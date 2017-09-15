(ns gorillalabs.tesla.component.reporters.riemann
    "Riemann reporter interface"
    (:require [metrics.core :refer [default-registry]]
      [metrics.reporters :as mrep])
    (:import java.util.concurrent.TimeUnit
      [com.codahale.metrics MetricRegistry Clock MetricFilter]
      [com.codahale.metrics.riemann RiemannReporter Riemann]))


(defn ^RiemannReporter reporter
      ([opts]
        (reporter default-registry opts))
      ([^MetricRegistry reg opts]
        (let [g (Riemann. (:host opts) (Integer/parseInt (:port opts)))
              b (RiemannReporter/forRegistry reg)]
             (when-let [^String s (:prefix opts)]
                       (.prefixedWith b s))
             (when-let [^Clock c (:clock opts)]
                       (.withClock b c))
             (when-let [^TimeUnit ru (:rate-unit opts)]
                       (.convertRatesTo b ru))
             (when-let [^TimeUnit du (:duration-unit opts)]
                       (.convertDurationsTo b du))
             (when-let [^MetricFilter f (:filter opts)]
                       (.filter b f))
             (.build b g))))

(defn start
      "Report all metrics to riemann periodically."
      [^RiemannReporter r ^long seconds]
      (mrep/start r seconds))

(defn stop
      "Stops reporting."
      [^RiemannReporter r]
      (mrep/stop r))



