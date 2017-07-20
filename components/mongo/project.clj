(defproject gorillalabs.tesla/mongo "0.5.2-SNAPSHOT"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A mongo component"
            :dependencies [[com.novemberain/monger "3.1.0"]
                           [gorillalabs.tesla/core :version]])
