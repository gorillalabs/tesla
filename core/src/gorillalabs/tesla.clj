(ns gorillalabs.tesla
    (:require
      [mount.core :as mnt]
      [beckon :as beckon]
      [clojure.tools.logging :as log]
      [environ.core :as env]
      [gorillalabs.tesla.component.appstate :as appstate]
      [gorillalabs.tesla.component.configuration :as config]
      [gorillalabs.tesla.component.keep-alive :as keep-alive]
      [gorillalabs.tesla.component.health :as health]
      ))

(defn wait! [conf]
      (if-let [wait-time (config/config conf [:wait-ms-on-stop])]
              (try
                (log/info "<- Waiting" wait-time "milliseconds.")
                (Thread/sleep wait-time)
                (catch Exception e (log/error e)))))



(defn stop []
      (beckon/reinit-all!)
      (log/info "<- System will be stopped. Setting lock.")
      (health/lock health/health)
      (wait! config/configuration)
      (log/info "<- Stopping system.")
      (mnt/stop))

(defn stop-on-signals [signals]
      (doseq [sig signals]
             (reset! (beckon/signal-atom sig) #{stop})))

(defn start
      ([]
        (log/info "-> Starting system")
        (stop-on-signals ["INT" "TERM"])
        (mnt/start)
        )
      ([& custom-components]
        (log/info "-> Starting parts of the system: " custom-components)
        (apply mnt/start custom-components)
        (stop-on-signals ["INT" "TERM"])))
