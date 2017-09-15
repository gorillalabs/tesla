(ns gorillalabs.tesla.component.timbre-logstash
    (:require [taoensso.timbre :as timbre]
      [mount.core :as mnt]
      [taoensso.timbre.appenders.3rd-party.logstash :as logstash]
      [gorillalabs.tesla.component.configuration :as config]))


(defn timbre-logstash-config [config]
      (let [host (get-in config [:timbre :host])
            port (get-in config [:timbre :port])
            level (get-in config [:timbre :log-level])]
           {:log-level     level
            :logstash-host host
            :logstash-port port}))

(defn- configure-timbre-logstash [config]
       (let [log-level (:log-level config)
             host (:logstash-host config)
             port (:logstash-port config)]
            (timbre/set-level! log-level)
            (timbre/merge-config! {:appenders {:logstash (logstash/logstash-appender host port)}})))

(defn- start []
       (let [timbre-config (timbre-logstash-config config/configuration)]
            (configure-timbre-logstash timbre-config)))

(defn stop []
      (timbre/set-config! [:appenders :logstash :enabled?] false))

(declare timbre-logstash)                                   ;; this is for Cursive IDE to pick up the symbol ;)
(mnt/defstate timbre-logstash
              :start (start)
              :stop (stop))
