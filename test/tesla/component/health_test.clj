(ns tesla.component.health-test
  (:require [clojure.test :refer :all]
            [tesla.component.health :as health]
            [tesla.util.test-utils :as u]
            [tesla.system :as system]
            [tesla.component.routes :as rts]
            [ring.mock.request :as mock]))

(defn- serverless-system [runtime-config]
  (dissoc
    (system/empty-system runtime-config)
    :server))

(deftest ^:unit should-turn-unhealthy-when-locked
  (u/with-started [started (serverless-system {})]
                  (testing "it is still healthy when not yet locked"
                    (let [handlers (rts/routes (:routes started))]
                      (is (= (handlers (mock/request :get "/health"))
                             {:body    "HEALTHY"
                              :headers {"Content-Type" "text/plain"}
                              :status  200}))))

                  (testing "when locked, it is unhealthy"
                    (let [handlers (rts/routes (:routes started))
                          _ (health/lock-application (:health started))]
                      (is (= (handlers (mock/request :get "/health"))
                             {:body    "UNHEALTHY"
                              :headers {"Content-Type" "text/plain"}
                              :status  503}))))))

(deftest ^:integration should-serve-health-under-configured-url
  (testing "use the default url"
    (u/with-started [started (serverless-system {})]
                    (let [handlers (rts/routes (:routes started))]
                      (is (= (handlers (mock/request :get "/health"))
                             {:body    "HEALTHY"
                              :headers {"Content-Type" "text/plain"}
                              :status  200})))))

  (testing "use the configuration url"
    (u/with-started [started (serverless-system {:health {:path "/my-health"}})]
                    (let [handlers (rts/routes (:routes started))]
                      (is (= (handlers (mock/request :get "/my-health"))
                             {:body    "HEALTHY"
                              :headers {"Content-Type" "text/plain"}
                              :status  200}))))))






