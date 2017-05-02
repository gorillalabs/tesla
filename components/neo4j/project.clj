(defproject gorillalabs.tesla/neo4j "0.4.55-SNAPSHOT"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A neo4j tesla-component"

            :dependencies [[gorillalabs.tesla/core :version]
                           [com.taoensso/timbre "4.10.0"]
                           [gorillalabs/neo4j-clj "0.3.1-SNAPSHOT"]
                           [org.neo4j/neo4j-cypher "3.1.4"]
                           ])

