(defproject gorillalabs.tesla.examples/calculator "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.4.0"]
   [com.roomkey/lein-v "7.1.0"]
   ]
  :description "a simple example of an application build with tesla."
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [gorillalabs.tesla/core nil]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [ch.qos.logback/logback-core "1.1.3"]
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :main ^:skip-aot gorillalabs.tesla.example)
