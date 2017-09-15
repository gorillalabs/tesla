(ns gorillalabs.tesla.component.quartzite
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [mount.core :as mnt]))

(declare quartzite)                                         ;; this is for Cursive IDE to pick up the symbol ;)

(mnt/defstate quartzite
              :start (-> (qs/initialize) qs/start)
              :stop (qs/shutdown quartzite))

#_(clojurewerkz.quartzite.jobs/defjob MyJob
                                      [ctx]
                                      (println "Running!"))

#_(def job (clojurewerkz.quartzite.jobs/build
             (clojurewerkz.quartzite.jobs/of-type MyJob)
             (clojurewerkz.quartzite.jobs/with-identity (clojurewerkz.quartzite.jobs/key "jobs.mine.1"))))

#_(def trigger (clojurewerkz.quartzite.triggers/build
                 (clojurewerkz.quartzite.triggers/with-identity (clojurewerkz.quartzite.triggers/key "triggers.1"))
                 (clojurewerkz.quartzite.triggers/start-now)
                 (clojurewerkz.quartzite.triggers/with-schedule (clojurewerkz.quartzite.schedule.simple/schedule
                                                                  (clojurewerkz.quartzite.schedule.simple/with-repeat-count 10)
                                                                  (clojurewerkz.quartzite.schedule.simple/with-interval-in-milliseconds 2000)))))

#_(qs/schedule gorillalabs.tesla.component.quartzite/quartzite job trigger)
