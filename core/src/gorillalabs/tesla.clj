(ns gorillalabs.tesla
  (:require
    [mount.core :as mnt]
    [beckon :as beckon]
    [clojure.tools.logging :as log]
    [environ.core :as env]
    [gorillalabs.tesla.component.appstate :as appstate]
    [gorillalabs.tesla.component.configuration :as config]
    [gorillalabs.tesla.component.metrics :as metrics]
    [gorillalabs.tesla.component.keep-alive :as keep-alive]
    [gorillalabs.tesla.component.handler :as handler]
    [gorillalabs.tesla.component.health :as health]
    ))

(defn wait! [conf]
  (if-let [wait-time (config/config conf [:wait-ms-on-stop])]
    (try
      (log/info "<- Waiting" wait-time "milliseconds.")
      (Thread/sleep wait-time)
      (catch Exception e (log/error e)))))


(let [default-components*
      {:config     #'config/configuration
       :keep-alive #'keep-alive/keep-alive
       :app-status #'appstate/appstate
       :health     #'health/health
       :metrics    #'metrics/metrics
       :handler    #'handler/handler
       ;       :httpkit    #'httpkit/httpkit
       ;       :quartzite  #'quartzite/quartzite
       ;       :mongo      #'mongo/mongo
       ;       :titan     #'titan/graph
       }]

  (defn default-components
    ([]
     default-components*)
    ([key]
     (default-components* key)))


  (defn stop []
    (beckon/reinit-all!)
    (log/info "<- System will be stopped. Setting lock.")
    ;   (health/lock-application (:health system))
    (wait! config/configuration)
    (log/info "<- Stopping system.")
    (mnt/stop))

  (defn start [custom-components & [more]]
    (log/info "-> Starting system")
    (apply mnt/start-with-args (or more {}) (concat (vals default-components) (vals custom-components)))
    (doseq [sig ["INT" "TERM"]]
      (reset! (beckon/signal-atom sig)
              #{stop}))))
