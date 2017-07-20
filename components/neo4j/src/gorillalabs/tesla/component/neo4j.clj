(ns gorillalabs.tesla.component.neo4j
  (:require [neo4j-clj.core :as neo4j]
            [clojure.tools.logging :as log]
            [gorillalabs.tesla.component.configuration :as config]
            [mount.core :as mnt]))

(defn- start []
  (let [url      (config/config config/configuration [:neo4j :url])
        user     (config/config config/configuration [:neo4j :user])
        password (config/config config/configuration [:neo4j :password])]
    (log/infof "-> starting neo4j (connecting to url %s as %s)" url user)
    (neo4j/connect url user password)))

(defn- stop [graph]
  (if graph
    (do
      (log/info "<- stopping neo4j component")
      (neo4j/disconnect graph))
    (log/info "<- neo4j component not running")))

(mnt/defstate graph
  :start (start)
  :stop (stop graph))
