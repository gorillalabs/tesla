(defproject gorillalabs.tesla/mongo :version
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A mongo component"
            :dependencies [[com.novemberain/monger "3.0.2"]
                           [gorillalabs.tesla/core :version]])
