;;;
;;; ######################################################################
;;; ## check, test and install
;;; 
;;;     lein monolith each do clean, check, test, install
;;;
;;;
;;; ## generate documentation
;;; 
;;;     lein monolith each do install, codox
;;;
;;;
;;; ## release using a parameter to "lein v update"
;;;
;;;   lein release minor
;;;   lein release major
;;;
;;; ######################################################################

(defproject gorillalabs/tesla "0.0.0"
  :description "basic microservice library"
  :vcs :git
  :deploy-repositories [["releases" :clojars]]

  :plugins [;; essential for the project structure, as we depend on inheritance of project.clj entries
            [lein-monolith "1.4.0"]
            [com.roomkey/lein-v "7.1.0"]]

  :middleware [leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :dependencies [[org.clojure/clojure "1.10.1"]]

  :exclusions [org.clojure/clojure
               org.slf4j/slf4j-nop
               org.slf4j/slf4j-log4j12
               log4j
               commons-logging/commons-logging]

  :codox {:metadata    {:doc/format :markdown}
          :doc-paths   ["README.md"]
          :output-path "doc/api"}


  :monolith
  {:inherit [:url
             :license
             :test-selectors
             :env
             :plugins
             :profiles
             :middleware
             :codox
             :repl-options
             :exclusions]

   :inherit-leaky
            [:dependencies
             :repositories
             :deploy-repositories
             :managed-dependencies]


   :project-dirs
            ["core/"
             #_"components/mongo"
             #_"components/titan/"
             #_"components/quartzite/"
             #_"components/sente/"
             #_"components/timbre-logstash"
             #_"components/aws-s3"
             #_"components/amqp"
             #_"components/neo4j"
             "examples/calculator"]}

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Releasing stuff
  ;;
  :scm {:name "git"
        :url  "https://github.com/gorillalabs/tesla"}

  :release-tasks [["vcs" "assert-committed"]
                  ["v" "update"]                            ;; compute new version & tag it
                  ["monolith" "each" "deploy"]
                  ["vcs" "push"]
                  ]

  :profiles {:install-for-with-all-repl {:middleware ^:replace []}
             :dev                       {:dependencies [[javax.servlet/servlet-api "2.5"]
                                                        [org.clojure/tools.namespace "0.2.11"]
                                                        [expectations "2.1.8"]
                                                        [ring-mock "0.1.5"]]}}
  )




;; use `LEIN_SNAPSHOTS_IN_RELEASE=true lein release` to release tesla
