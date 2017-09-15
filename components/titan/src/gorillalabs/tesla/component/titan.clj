(ns gorillalabs.tesla.component.titan
  (:require [clojurewerkz.titanium.graph :as tg]
            [clojure.tools.logging :as log]
            [gorillalabs.tesla.component.configuration :as config]
            [mount.core :as mnt]))

(defn titan-config [config]
  (let [titan-cfg {"storage.backend"                               "cassandrathrift"
                   "index.search.backend"                          "lucene"
                   "index.search.directory"                        "/tmp/searchIndex"
                   "attributes.custom.attribute1.attribute-class"  "clojure.lang.Keyword"
                   "attributes.custom.attribute1.serializer-class" "hamburg.cypp.KeywordSerializer"}
        res (merge titan-cfg config)]
    res))

(defn- start []
  (let [server-config (titan-config (config/config config/configuration [:titan]))]
    (log/infof "-> starting titan (storage @ %s, search index @ %s)"
               (get server-config "storage.hostname" "[not set]")
               (get server-config "index.search.hostname" "[not set]"))
    (tg/open server-config)))

(defn- stop [graph]
  (if graph
    (do
      (log/info "<- stopping titan")
      (tg/close graph))
    (log/info "<- titan not running")))

(mnt/defstate graph
              :start (start)
              :stop (stop graph))
