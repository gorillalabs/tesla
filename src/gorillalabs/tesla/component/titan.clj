(ns gorillalabs.tesla.component.titan
  (:require
   [clojurewerkz.titanium.graph :as tg]
   [clojure.tools.logging :as log]
   [gorillalabs.tesla.component.configuration :as config]
   [mount.core :as mnt]))

(defn titan-config [config]
  {"storage.backend"  "cassandrathrift"
   "index.search.backend" "lucene"
   "index.search.directory" "/tmp/searchIndex"
   })

(defn- start []
  (log/info "-> starting titan")
  (let [server-config (titan-config config/configuration)
        graph (tg/open server-config)]
    graph))

(defn- stop [graph]
  (if graph
    (do
      (log/info "<- stopping titan")
      (tg/shutdown graph))
    (log/info "<- titan not running")))


(mnt/defstate graph
              :start (start)
              :stop (stop graph))

