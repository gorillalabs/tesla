(ns gorillalabs.tesla.example.calculating-test
  (:require [clojure.test :refer :all]
            [gorillalabs.tesla.example.calculating :as calculating]
            [gorillalabs.tesla.example :as example]
            [gorillalabs.tesla :as tesla]))
(deftest ^:unit calculations-should-be-counted
  (tesla/start)
  (is (= (calculating/calculate! calculating/calculator "foo") "FOO"))
  (is (= (calculating/calculate! calculating/calculator "bar") "BAR"))
  (is (= @(calculating/calculations calculating/calculator) 2))
  (tesla/stop))