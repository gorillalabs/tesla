(defproject gorillalabs.tesla/amqp "0.4.56-SNAPSHOT"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A component to connect to amqp queues and exchanges."
  :dependencies [[gorillalabs.tesla/core :version]
                 [com.novemberain/langohr "3.6.1"]
                 [org.xerial.snappy/snappy-java "1.1.2.4"] ;; keep this version
                 [com.taoensso/nippy "2.12.1"]
                 [commons-codec "1.10"]  ;; keep this version
                 [commons-lang "2.6"]  ;; keep this version
                 [clojure-future-spec "1.9.0-alpha10"];; remove when upgrading to clojure 1.9
                 ])
