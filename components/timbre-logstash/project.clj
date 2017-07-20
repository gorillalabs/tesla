(defproject gorillalabs.tesla/timbre-logstash "0.5.0"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A logastash component"

  :dependencies [[com.taoensso/timbre "4.10.0"]
                 [gorillalabs.tesla/core :version]
                 [commons-codec "1.10"]
                 ])
