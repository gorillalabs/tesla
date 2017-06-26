(defproject gorillalabs.tesla/neo4j "0.4.55-SNAPSHOT"
  :plugins [[lein-modules "0.3.11"]]
  :modules {:parent "../.."}
  :description "A neo4j tesla-component"

  :dependencies [[gorillalabs.tesla/core :version]
                 [com.taoensso/timbre "4.6.0"]
                 [gorillalabs/neo4j-clj "0.3.3-SNAPSHOT"]])

