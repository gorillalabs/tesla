(ns gorillalabs.tesla.component.socket
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.immutant :refer (get-sch-adapter)]
            [mount.core :as mnt]
            [clojure.tools.logging :as log]))

(declare socket)

(defn default-user-id-fn [ring-req]
  (log/warn "There is no user-id-fn specified! Using client-id.")
  (:client-id ring-req))

(defn- start [{user-id-fn :user-id-fn :or {user-id-fn default-user-id-fn} :as args}]
  (log/info "-> Starting sente socket w/ " args)
  (sente/make-channel-socket! (get-sch-adapter) {:packet :edn :user-id-fn user-id-fn}))

(defn- stop [self]
  (log/info "<- Stopping sente socket."))

(mnt/defstate socket
              :start (start (mnt/args))
              :stop (stop socket))
