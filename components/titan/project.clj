(defproject gorillalabs.tesla/titan
  "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.2.0"]
   ]
  :description "A titan component"
  :dependencies [[gorillalabs.tesla/core nil]
                 [gorillalabs/titanium "1.0.6"]
                 [gorillalabs/titan-cassandra "1.1.1"]
                 [gorillalabs/titan-lucene "1.1.1"]
                 [org.xerial.snappy/snappy-java "1.1.2.6"]  ;; keep this version
                 [commons-codec "1.10"]                     ;; keep this version
                 [commons-lang "2.6"]                       ;; keep this version
                 ]
  :java-source-paths ["src/java"])
