(defproject gorillalabs.tesla/titan "0.4.55-SNAPSHOT"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A titan component"
            :dependencies [[gorillalabs.tesla/core :version]
                           [gorillalabs/titanium "1.0.6"]
                           [gorillalabs/titan-cassandra "1.1.1"]
                           [gorillalabs/titan-lucene "1.1.1"]
                           [org.xerial.snappy/snappy-java "1.1.2.4"] ;; keep this version
                           [commons-codec "1.10"]  ;; keep this version
                           [commons-lang "2.6"]  ;; keep this version
                           ]
    :java-source-paths ["src/java"])
