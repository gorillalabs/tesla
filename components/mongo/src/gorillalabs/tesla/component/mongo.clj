(ns gorillalabs.tesla.component.mongo
  (:require [monger.core :as mg]
            [monger.credentials :as mg-cred]
            [mount.core :as mnt])
  (:import [com.mongodb MongoOptions ServerAddress WriteConcern]))


(defn- start []
  (let [^MongoOptions opts (mg/mongo-options {:threads-allowed-to-block-for-connection-multiplier 300})
        ^ServerAddress sa (mg/server-address "127.0.0.1" 27017)
        cred (mg-cred/create "root" "admin" "password")
        conn (mg/connect sa opts cred)]
    conn))

(defn- stop [mongo]
  (mg/disconnect mongo)
  )

(declare mongo)                                             ;; this is for Cursive IDE to pick up the symbol ;)
(mnt/defstate mongo
              :start (start)
              :stop (stop mongo))

(defn getDatabase [name]
  (mg/get-db mongo name))

