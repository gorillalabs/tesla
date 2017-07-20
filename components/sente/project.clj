(defproject gorillalabs.tesla/sente "0.5.0"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "sente websocket component"
            :dependencies [[gorillalabs.tesla/core :version]
                           [com.taoensso/sente "1.11.0"]])
