(ns gorillalabs.tesla.component.metrics-test
    (:require [gorillalabs.tesla.component.metrics :as metrics]
      [gorillalabs.tesla.component.configuration :as configuration]
      [clojure.test :refer :all]
      )
    (:import (com.codahale.metrics MetricRegistry ConsoleReporter)
      (com.codahale.metrics.graphite GraphiteReporter)))



(deftest test-start-metrics
         (testing "Empty configuration should not start any reporters, but have a registry."
                  (with-redefs [configuration/configuration {}]
                               (let [{:keys [registry reporters] :as metrics} (#'metrics/start)]
                                    (is (instance? MetricRegistry registry))
                                    (is (empty? reporters))
                                    (#'metrics/stop metrics)
                                    )))

         (testing "Should be able to start single reporter"
                  (with-redefs [configuration/configuration {:metrics {:reporter :console}}]
                               (let [{:keys [registry reporters] :as metrics} (#'metrics/start)]
                                    (is (instance? MetricRegistry registry))
                                    (is (coll? reporters))
                                    (is (instance? ConsoleReporter (first reporters)))
                                    (#'metrics/stop metrics)
                                    )))

         (testing "Should be able to start multiple reporters"
                  (with-redefs [configuration/configuration {:metrics {:reporter [:console :graphite]}}]
                               (let [{:keys [registry reporters] :as metrics} (#'metrics/start)]
                                    (is (instance? MetricRegistry registry))
                                    (is (coll? reporters))
                                    (is (instance? ConsoleReporter (first reporters)))
                                    (is (instance? GraphiteReporter (second reporters)))
                                    (#'metrics/stop metrics)
                                    ))))
