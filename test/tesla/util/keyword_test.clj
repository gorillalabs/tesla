(ns tesla.util.keyword-test
  (:require [clojure.test :refer :all]
            [tesla.util.keyword :as kwutil]))

(deftest ^:unit should-map-keys-to-sanitized-keywords
  (is (= (kwutil/sanitize-keywords {"thats.a.property" "a value"})
         {:thats-a-property "a value"})))