(ns gorillalabs.tesla.component.router
  (:require [mount.core :as mnt]
            [clojure.tools.logging :as log]
            [clojure.core.async :as async :refer (<! <!! >! >!! put! chan go go-loop close!)]
            [gorillalabs.tesla.component.socket :as socket]
            [taoensso.sente :as sente]))

(declare router)

(defn- event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [event-msg-channel]
  (go-loop [event-msg (<! event-msg-channel)]
    (when event-msg
      (let [{:keys [id event ?reply-fn]} event-msg]
        (go (if-let [event-fn (get @(:events router) id)]
              (event-fn event-msg)
              (do (log/infof "Unhandled event: %s" id)
                  (when ?reply-fn
                    (?reply-fn {:umatched-event event})))))))
    (recur (<! event-msg-channel))))

(defn register
  ([id callback]
   (register router id callback))
  ([router-component id callback]
   (swap! (:events router-component) assoc id callback)))

(defn deregister
  ([id]
   (deregister router id))
  ([router-component id]
   (swap! (:events router-component) dissoc id)))

;; This is the bridge between incoming HTTP requests and our our
;; little event channel, from which we then further dispatch the
;; then-called "events" to their handlers.
(defn- move-request-to-queue [request]
  (go (>! (:event-msg-channel router) request)))

(defn- start []
  (log/info "-> Starting sente router.")
  (let [event-msg-channel (chan)]
    (event-msg-handler event-msg-channel)
    {:event-msg-channel event-msg-channel
     :events            (atom {})
     :router            (sente/start-server-chsk-router! (:ch-recv socket/socket) move-request-to-queue)}))

(defn- stop [self]
  (log/info "<- Stopping sente router.")
  (when-let [router (:router self)] (router))
  (when-let [channel (:event-msg-channel self)] (close! channel)))

(mnt/defstate router
              :start (start)
              :stop (stop router))
