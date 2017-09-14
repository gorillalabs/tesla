(defproject gorillalabs.tesla/mongo 
"0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.2.0"]
   ]
            :description "A mongo component"
            :dependencies [[com.novemberain/monger "3.1.0"]
                           [gorillalabs.tesla/core nil]])
