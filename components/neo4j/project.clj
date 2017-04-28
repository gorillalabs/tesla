(defproject neo4j "0.1.0-SNAPSHOT"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A neo4j component"

            :dependencies [[com.taoensso/timbre "4.6.0"]
                           [neo4j-clj "0.3.1-SNAPSHOT"]
                           ])

