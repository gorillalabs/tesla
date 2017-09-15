(defproject gorillalabs.tesla/sente "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.2.0"]
   ]
   
            :description "sente websocket component"
            :dependencies [[gorillalabs.tesla/core nil]
                           [com.taoensso/sente "1.11.0"]])
