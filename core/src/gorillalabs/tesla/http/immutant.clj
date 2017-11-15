(ns gorillalabs.tesla.http.immutant
    (:require [immutant.web :as web]
      [clojure.tools.logging :as log]
      [gorillalabs.tesla.component.configuration :as config]))

(def default-port 3000)

(defn server-config [config]
      (-> (if (get-in config [:immutant.web :keystore])
            (immutant.web.undertow/options
              :ssl-port (get-in config [:immutant.web :ssl-port] default-port)
              :keystore (get-in config [:immutant.web :keystore])
              :key-password (get-in config [:immutant.web :key-password]))
            {})
          (assoc :virtual-host (get-in config [:immutant.web :virtual-host])
                 :host (get-in config [:immutant.web :bind] "0.0.0.0"))))


(defn start [handler]
      (log/info "-> starting immutant.web")
      (let [server-config (server-config config/configuration)]
           (log/info "Starting immutant.web")
           (immutant.web/run handler server-config)))

(defn stop [server]
      (let [timeout (config/config config/configuration [:httpkit-timeout] 100)]
           (if server
             (do
               (log/info "<- stopping immutant.web")
               (immutant.web/stop server))
             (log/info "<- stopping immutant.web"))))