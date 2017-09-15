(ns gorillalabs.tesla.component.metrics
    (:require
      [mount.core :as mnt]
      [metrics.core :as metrics]
      [metrics.timers :as timers]
      [metrics.counters :as counters]
      [metrics.gauges :as gauges]
      [metrics.meters :as meters]
      [metrics.reporters.graphite :as graphite]
      [metrics.reporters.console :as console]
      [gorillalabs.tesla.component.reporters.riemann :as riemann]
      [clojure.tools.logging :as log]
      [gorillalabs.tesla.component.configuration :as config])
    (:import
      (com.codahale.metrics MetricFilter)
      (java.util.concurrent TimeUnit)))

(defmulti start-reporter! (fn [reporter _ _] reporter))

(defmethod start-reporter! :riemann [_ registry config]
           (let [reporter (riemann/reporter registry
                                            {:host          (config/config config [:metrics :reporter :riemann :host] "127.0.0.1")
                                             :port          (config/config config [:metrics :reporter :riemann :port] "5555")
                                             :service       (config/config config [:metrics :reporter :riemann :service] "*unknown-service*")
                                             :rate-unit     TimeUnit/SECONDS
                                             :duration-unit TimeUnit/MILLISECONDS
                                             :filter        MetricFilter/ALL})]
                (log/info "-> starting riemann metrics reporter.")
                (riemann/start reporter (int (config/config config [:metrics :riemann :interval-seconds] 10)))
                reporter))

(defmethod start-reporter! :graphite [_ registry config]
           (let [prefix (fn prefix [config]
                            (str (config/config config [:metrics :graphite :prefix]) "." (config/external-hostname config)))
                 reporter (graphite/reporter registry
                                             {:host          (config/config config [:metrics :graphite :host])
                                              :port          (int (config/config config [:metrics :graphite :port] 2003))
                                              :prefix        (prefix config)
                                              :rate-unit     TimeUnit/SECONDS
                                              :duration-unit TimeUnit/MILLISECONDS
                                              :filter        MetricFilter/ALL})]
                (log/info "-> starting graphite reporter.")
                (graphite/start reporter (int (config/config config [:metrics :graphite :interval-seconds] 10)))
                reporter))

(defmethod start-reporter! :console [_ registry config]
           (let [reporter (console/reporter registry {})]
                (log/info "-> starting console reporter.")
                (console/start reporter (int (config/config config [:metrics :console :interval-seconds] 10)))
                reporter))

(defn- start-reporters! [registry config]
       (let [reporter-or-reporters (config/config config [:metrics :reporter])
             reporters (if (or (nil? reporter-or-reporters) ;; might be nil
                               (coll? reporter-or-reporters))
                         reporter-or-reporters
                         (vector reporter-or-reporters))]
            (doall (map
                     #(start-reporter! %1 registry config)
                     reporters
                     ))))



(defn- start []
       (log/info "-> starting metrics.")
       (let [registry (metrics/new-registry)
             config config/configuration]
            {:registry  registry
             :reporters (start-reporters! registry config)}))

(defn- stop [metrics]
       (log/info "<- stopping metrics")
       (when-let [reporter (:reporters metrics)]
                 (doall (map (fn stop-reporter [r]
                                 (log/info "stopping " r)
                                 (.stop r)) reporter)))
       metrics)

(declare metrics)
(mnt/defstate ^{:on-reload :noop}
              metrics
              :start (start)
              :stop (stop metrics))

(defn gauge! [name gauge-callback-fn]
      (gauges/gauge-fn (:registry metrics) [name] gauge-callback-fn))

(defn timer! [name]
      (timers/timer (:registry metrics) [name]))

(defn counter! [name]
      (counters/counter (:registry metrics) [name]))

(defn meter! [name]
      (meters/meter (:registry metrics) name))