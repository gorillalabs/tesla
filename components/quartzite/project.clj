(defproject gorillalabs.tesla/quartzite :version
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A quartzite component"
            :dependencies [[gorillalabs.tesla/core :version]
                           [clojurewerkz/quartzite "2.0.0"]
                           ])
