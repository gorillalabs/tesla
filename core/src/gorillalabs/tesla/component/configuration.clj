(ns gorillalabs.tesla.component.configuration
    "This component is responsible for loading the configuration."
    (:require
      [mount.core :as mnt]
      [clojure.tools.logging :as log]
      [gorillalabs.config :as config]
      [environ.core :as environ]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Access functions

(defn config
      ([config]
        config)
      ([config key-path]
        (get-in config key-path))
      ([config key-path default]
        (get-in config key-path default)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Special access functions

(defn external-hostname [config-component]
      ;; old function was otto-specific
      (config config-component [:hostname] "localhost")
      )

(defn external-port [config-component]
      ;; old function was otto-specific
      (config config-component [:external-port]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Entry point (after mount)

(defn- load-config []
       (config/init (str (environ/env :env))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Component related stuff


(defn start-configuring []
      (log/info "-> starting config")
      (let [conf (merge (load-config)
                        {:version {:version "test.version"  ;; load this from Manifest?
                                   :commit  "test.githash"}})]
           conf))

(declare configuration)                                     ;; this is for Cursive IDE to pick up the symbol ;)
(mnt/defstate configuration :start (start-configuring))
