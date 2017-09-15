(ns gorillalabs.tesla.component.keep-alive
    "This component is responsible for keeping the system alive by creating a non-deamonized noop thread."
    (:require [clojure.tools.logging :as log]
      [mount.core :as mnt]))

(defn do-nothing [running?]
      (while @running?
             (try
               (Thread/sleep 60000)
               (catch InterruptedException i
                 (log/debug "keepalive thread sleep interrupted!"))
               (catch Exception e (throw e)))))

(defn start-keep-alive-thread [running?]
      (let [thread (Thread. (partial do-nothing running?) "keepalive")]
           (.start thread)
           thread))


(defn- stop [keep-alive]
       (log/info "<- stopping keepalive thread.")
       (try
         (when-let [running? (:running? keep-alive)]
                   (reset! running? false)
                   (.interrupt (:thread keep-alive)))       ;; try the "not-so-unfriendly"-method first.
         (Thread/sleep 1000)
         (.stop (:thread keep-alive))                       ;; and finally kill it.
         (catch Exception e
           (log/debug e "Error stopping keepalive thread.")))
       keep-alive)

(defn- start []
       (log/info "-> starting keepalive thread.")
       (let [running? (atom true)]
            {:running? running?
             :thread   (start-keep-alive-thread running?)}))

(declare keep-alive)
(mnt/defstate keep-alive
              :start (start)
              :stop (stop keep-alive))
