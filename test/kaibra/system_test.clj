(ns kaibra.system-test
  (:require [clojure.test :refer :all]
            [kaibra.system :as system]
            [mount.core :as mnt]
            [kaibra.stateful.health :refer [health]]))

(deftest ^:unit should-start-base-system-and-shut-it-down
  (testing "start then shutdown using own method"
    (system/start)
    (system/stop)
    (is (= "look ma, no exceptions" "look ma, no exceptions")))

  (testing "start then shutdown using method from library"
    (mnt/start)
    (mnt/stop)
    (is (= "look ma, no exceptions" "look ma, no exceptions"))))

;
;  (testing "it waits on stop"
;    (u/with-started
;      [started (system/base-system {:wait-seconds-on-stop 1})]
;      (let [has-waited (atom false)]
;        (with-redefs [system/wait! (fn [_] (reset! has-waited true))]
;          (let [healthcomp (:health started)
;                _ (system/stop started)]
;            (is (= @(:locked healthcomp) true))
;            (is (= @has-waited true))))))))
;
;
;(deftest ^:integration should-substitute-env-variables-while-reading
;  (with-redefs [env/env {:my-custom-status-url "/custom/status/path" :prop-without-fallback "some prop value"}]
;    (u/with-started [started (system/base-system {})]
;                    (testing "should load the status-path property from edn"
;                      (is (= "/custom/status/path"
;                             (:status-url (configuring/load-config-from-edn-files)))))
;
;                    (testing "should point to edn-configured custom status url"
;                      (let [handlers (handler/handler (:handler started))
;                            response (handlers (mock/request :get "/custom/status/path"))]
;                        (is (= 200 (:status response)))))))
;
;  (u/with-started [started (system/base-system {})]
;                  (testing "should fallback to default for status path"
;                    (is (= "/status"
;                           (:status-url (configuring/load-config-from-edn-files)))))))
