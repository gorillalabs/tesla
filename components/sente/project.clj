(defproject gorillalabs.tesla/sente :version
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "sente websocket component"
            :dependencies [[gorillalabs.tesla/core :version]
                           [com.taoensso/sente "1.9.0"]])
