(defproject gorillalabs.tesla/quartzite "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.3.0"]
   ]
  :description "A quartzite component"
  :dependencies [[gorillalabs.tesla/core nil]
                 [clojurewerkz/quartzite "2.1.0"]
                 ])
