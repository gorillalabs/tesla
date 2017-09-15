(ns gorillalabs.tesla.component.authorization-test
    (:require [clojure.test :refer :all]
      [buddy.auth.protocols :as proto]
      [gorillalabs.tesla.component.authorization :as auth]
      [gorillalabs.tesla.component.configuration :as config]))

(deftest test-authorize
         (let [test-state {:authorization {:secret   "12345678901234567890123456789012"
                                           :options  {:alg :a256kw
                                                      :enc :a128gcm}
                                           :authdata {:admin {:roles    [:some_role]
                                                              :password "password"
                                                              }}}}
               create-authorization #'auth/create-authorization]
              (with-redefs [config/configuration test-state]
                           (with-redefs [auth/authorization (create-authorization)]
                                        (testing "testing cornercase credentials"
                                                 (let [result (auth/authorize nil nil)]
                                                      (is (= result nil)))
                                                 (let [result (auth/authorize [] [])]
                                                      (is (= result nil)))
                                                 (let [result (auth/authorize 0 0)]
                                                      (is (= result nil)))
                                                 (let [result (auth/authorize 1 1)]
                                                      (is (= result nil)))
                                                 (let [result (auth/authorize "" "")]
                                                      (is (= result nil))))

                                        (testing "test invalid credentials"
                                                 (let [result (auth/authorize "han" "solo")]
                                                      (is (= result nil))))


                                        (testing "test correct string username and password, check for valid token"
                                                 (let [result (auth/authorize "admin" "password")
                                                       backend (:backend auth/authorization)
                                                       request {:headers {"authorization" (str "Token " result)}}]
                                                      (is (not= result nil))
                                                      (is (some->> request
                                                                   (proto/-parse backend)
                                                                   (proto/-authenticate backend request)))
                                                      ))
                                        ))))




