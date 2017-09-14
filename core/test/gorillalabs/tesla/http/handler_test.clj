(ns gorillalabs.tesla.http.handler-test
  (:require [clojure.test :refer :all]
            [gorillalabs.tesla.http.handler :refer :all])
  )


(deftest test-merge-config
         (is (= {:a 1, :b {:x 42, :y 4}, :c 3} (merge-config {:a 1 :b {:x 12}} {:c 3 :b {:x 42 :y 4}}))))
