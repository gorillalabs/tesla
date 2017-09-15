(ns gorillalabs.tesla.http.httpkit-test
    (:require [clojure.test :refer :all]
      [gorillalabs.tesla.http.httpkit :refer :all]))


(deftest test-server-config
         (testing "testing cornercase credentials"
                  (is (= (:port (server-config {})) 3000))
                  (is (= (:port (server-config {:httpkit {:port 3002}})) 3002))
                  ))
