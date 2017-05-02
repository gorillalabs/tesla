(defproject gorillalabs.tesla/aws-s3 "0.4.55-SNAPSHOT"
            :plugins [[lein-modules "0.3.11"]]
            :modules {:parent "../.."}
            :description "A component to enable simple access to aws s3 stores."
  :dependencies [[gorillalabs.tesla/core :version]
                 [gorillalabs/clj-aws-s3 "0.3.10"]
                 [org.xerial.snappy/snappy-java "1.1.2.4"] ;; keep this version
                 [commons-codec "1.10"]  ;; keep this version
                 [commons-lang "2.6"]  ;; keep this version
                 ])
