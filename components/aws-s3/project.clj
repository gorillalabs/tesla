(defproject gorillalabs.tesla/aws-s3 "0.0.0"
  :monolith/inherit true
  :middleware [leiningen.v/dependency-version-from-scm
               leiningen.v/version-from-scm
               leiningen.v/add-workspace-data]

  :plugins
  [[lein-monolith "1.0.1"]
   [com.roomkey/lein-v "6.3.0"]
   ]

  :description "A component to enable simple access to aws s3 stores."
  :dependencies [[gorillalabs.tesla/core nil]
                 [gorillalabs/clj-aws-s3 "0.3.11"]
                 [org.xerial.snappy/snappy-java "1.1.7.1"]  ;; keep this version
                 [commons-codec "1.11"]                     ;; keep this version
                 [commons-lang "2.6"]                       ;; keep this version
                 ])
