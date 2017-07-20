(defproject gorillalabs.tesla/timbre-logstash :version
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A logastash component"

            :dependencies [[com.taoensso/timbre "4.6.0"]
                           [gorillalabs.tesla/core :version]])
