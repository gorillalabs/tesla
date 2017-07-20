(defproject gorillalabs/tesla "0.4.57-SNAPSHOT"
  :description "basic microservice."
  :vcs :git
  :plugins [[lein-modules "0.3.11"]
            [lein-pprint "1.1.1"]]
  :modules {:dirs       ["core/" "components/mongo" "components/titan/" "components/quartzite/" "components/sente/" "components/timbre-logstash" "components/aws-s3" "components/amqp" "components/neo4j"]
            :subprocess nil
            :inherited  {:url                 "https://github.com/gorillalabs/tesla"
                         :license             {:name "Apache License 2.0"
                                               :url  "http://www.apache.org/license/LICENSE-2.0.html"}
                         :deploy-repositories [["releases" :clojars]]
                         :scm                 {:dir ".."}
                         :exclusions          [org.clojure/clojure
                                               org.slf4j/slf4j-nop
                                               org.slf4j/slf4j-log4j12
                                               log4j
                                               commons-logging/commons-logging]}
                         
            :versions   {org.clojure/clojure       "1.8.0"
                         org.clojure/tools.logging "0.3.1"}}

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Releasing stuff
  ;;
  :scm {:name "git"
        :url  "https://github.com/gorillalabs/tesla"}

  :profiles {:provided
             {:dependencies [[org.clojure/clojure "_"]]}
             :dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                                      [org.clojure/tools.namespace "0.2.11"]
                                      [expectations "2.1.8"]
                                      [ring-mock "0.1.5"]]
                       :plugins      [[lein-pprint "1.1.1"]
                                      [lein-ancient "0.6.8"  :exclusions [org.clojure/clojure]]
                                      [lein-marginalia "0.9.0"]
                                      [lein-kibit "0.1.2"]
                                      [jonase/eastwood "0.2.3"  :exclusions [org.clojure/clojure]]]}}
                                      
                       

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["modules" "change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "v"]
                  ["modules" "deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["modules" "change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]])

;; You always work on a SNAPSHOT version locally, but you shouldn't push one to a repository like Clojars.
;; `lein modules install` will install into your local repository, necessary to start coding.
;; `lein modules checkout` will create checkout directories where necessary
;; `lein modules do check, test` to run checks
;; `lein modules ancient upgrade` to upgrade dependencies
;; `lein modules eastwood` to lint code
;; use `LEIN_SNAPSHOTS_IN_RELEASE=true lein release` to release tesla
