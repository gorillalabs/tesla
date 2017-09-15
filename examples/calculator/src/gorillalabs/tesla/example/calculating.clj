(ns gorillalabs.tesla.example.calculating
  (:require [gorillalabs.tesla.component.metrics :as metrics]
            [gorillalabs.tesla.component.appstate :as appstate]
            [clojure.tools.logging :as log]
            [de.otto.status :as s]
            [metrics.timers :as timers]
            [mount.core :as mnt]))

(declare calculator)

(defn example-calculation-function [input]
  (.toUpperCase input))

;; status turns warning after 10 calculations. Because license expired.

(defn calculations [self]
  (:calculations self))

(defn calculate! [self input]
  (timers/time! (:timer self)
                (swap! (:calculations self) inc)
                ((:fun self) input)))

(defn- status-fun
  []
  (println calculator)
  (if (> 10 @(calculations calculator))
    (s/status-detail :calculator :ok "less than 10 calculations performed")
    (s/status-detail :calculator :warning "more than 10 calculations perormed. Renew license.")))

(defn start []
  (log/info "-> starting example calculator.")
  (let [new-self
        {:timer        (metrics/timer! "calculations")
         :calculations (atom 0)
         :fun          example-calculation-function}]
    (appstate/register-state-fn appstate/appstate
                                status-fun)
    new-self))

(defn stop [self]
  (log/info "<- stopping example calculator.")
  (reset! (:calculations self) 0)
  self)


(mnt/defstate calculator
              :start (start)
              :stop (stop calculator))