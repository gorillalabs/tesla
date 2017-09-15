(defproject gorillalabs.tesla/amqp "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.2.0"]
   ]
  :description "A component to connect to amqp queues and exchanges."
  :dependencies [[gorillalabs.tesla/core nil]
                 [com.novemberain/langohr "3.7.0"]
                 [org.xerial.snappy/snappy-java "1.1.2.6"]  ;; keep this version
                 [com.taoensso/nippy "2.13.0"]
                 [commons-codec "1.10"]                     ;; keep this version
                 [commons-lang "2.6"]                       ;; keep this version
                 [clojure-future-spec "1.9.0-alpha10"]      ;; remove when upgrading to clojure 1.9
                 ])
