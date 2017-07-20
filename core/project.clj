(defproject gorillalabs.tesla/core "0.4.56"
            :plugins [[lein-modules "0.3.11"]]
            :description "basic microservice."
            :dependencies [[mount "0.1.10"]
                           [gorillalabs/config "1.0.4"]
                           [org.clojure/data.json "0.2.6"]
                           [beckon "0.1.1"]
                           [environ "1.0.3"]
                           [clj-time "0.11.0"]
                           [joda-time "2.9.3"] ;; fix a version (otherwise we have version clashes)


                           ;; Logging
                           [org.clojure/tools.logging "0.4.0"]
                           [org.slf4j/slf4j-api "1.7.21"]
                           [org.slf4j/log4j-over-slf4j "1.7.21"]
                           [ch.qos.logback/logback-core "1.1.7"]
                           [ch.qos.logback/logback-classic "1.1.7"]
                           [org.codehaus.janino/janino "2.7.8"]

                           ;; HttpKit
                           [http-kit "2.1.19"]

                           ;; Authentication
                           [ring/ring-json "0.4.0"]
                           [buddy "0.13.0"]
                           [cheshire "5.6.3"]  ;; fix a version (otherwise we have version clashes)
                           [org.clojure/tools.reader "0.10.0"] ;; fix a version (otherwise we have version clashes)


                           ;; io
                           [ring/ring-core "1.4.0"]
                           [ring/ring-defaults "0.2.0"]
                           [bidi "2.0.9"]

                           ;; status
                           [de.otto/status "0.1.0"]

                           ;; metrics
                           [metrics-clojure "2.6.1"]
                           [metrics-clojure-graphite "2.6.1"]
                           [io.riemann/metrics3-riemann-reporter "0.4.2"]

                           ;; telemetry
                           [org.clojure/core.async "0.3.442"]
                           [riemann-clojure-client "0.4.5"]
                           [io.riemann/riemann-java-client "0.4.5"]
                           ]


            :exclusions [org.clojure/clojure
                         org.slf4j/slf4j-nop
                         org.slf4j/slf4j-log4j12
                         log4j
                         commons-logging/commons-logging]

            :test-paths ["test" "test-resources" "test-utils"]
            :test-selectors {:default     (constantly true)
                             :integration :integration
                             :unit        :unit
                             :all         (constantly true)}


            :profiles {:uberjar {:aot :all}
                       :dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                                                [org.clojure/tools.namespace "0.2.11"]
                                                [expectations "2.1.8"]
                                                [ring-mock "0.1.5"]]
                                 :plugins      [[lein-pprint "1.1.1"]
                                                [lein-ancient "0.6.8"  :exclusions [org.clojure/clojure]]
                                                [lein-marginalia "0.8.0"]
                                                [lein-environ "1.0.3"]
                                                [jonase/eastwood "0.2.3"  :exclusions [org.clojure/clojure]]
                                                ]

                                 :env          {:system "SYSTEM"
                                                :env    "ENV"}}}
            )
