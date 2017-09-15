(defproject gorillalabs.tesla/timbre-logstash
  "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.2.0"]
   ]
  :description "A logastash component"

  :dependencies [[com.taoensso/timbre "4.10.0"]
                 [gorillalabs.tesla/core nil]
                 [commons-codec "1.10"]
                 ])
