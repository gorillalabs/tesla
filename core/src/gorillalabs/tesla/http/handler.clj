(ns gorillalabs.tesla.http.handler
    (:require [clojure.tools.logging :as log]
      [ring.middleware.defaults :as ring-defaults]
      [ring.middleware.json :refer [wrap-json-response wrap-json-params wrap-json-body]]
      [ring.util.response :refer [content-type]]
      [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
      [buddy.auth :refer [authenticated? throw-unauthorized]]
      ))

(defn merge-sub-config [defaults key sub-overrides]
      (if (map? sub-overrides)
        (update-in defaults [key] merge sub-overrides)
        (assoc defaults key sub-overrides)))

(defn merge-config [defaults overrides]
      (reduce-kv merge-sub-config defaults overrides))






(defn- exception_caught [& _] {:status 500 :body {:message "Internal error"}})

(defn- wrap-exception-handling
       "handles all not yet caught exceptions"
       [handler]
       (fn [request]
           (try
             (handler request)
             (catch Exception e
               (do (log/error e (str "caught exception: " (.getMessage e)))
                   (exception_caught))))))

(defn- wrap-enforce-json-content-type
       "sets the content type to application/json, regardless to any existing value"
       [handler]
       (fn [request]
           (content-type (handler request) "application/json")))

(defn- wrap-block-not-authenticated-requests
       [handler]
       (fn [request]
           (if-not (authenticated? request)
                   (throw-unauthorized)
                   (handler request))))

(defn- wrap-common-api-handler
       [handler & [config]]
       (-> handler
           (wrap-exception-handling)
           (wrap-enforce-json-content-type)
           (ring-defaults/wrap-defaults (merge-config ring-defaults/secure-api-defaults config))
           (wrap-json-body)
           (wrap-json-params)
           (wrap-json-response)))

(defn wrap-secure-api [handler authorization & [config]]
      (-> handler
          (wrap-block-not-authenticated-requests)
          (wrap-authentication (:backend authorization))
          (wrap-authorization (:backend authorization))
          (wrap-common-api-handler config)))


(defn wrap-insecure-api [handler & [config]]
      (-> handler
          (wrap-common-api-handler config)))


(defn wrap-site [handler & [config]]
      (ring-defaults/wrap-defaults
        handler
        (-> ring-defaults/secure-site-defaults
            (assoc
              :session false
              :cookies false
              :static false
              :proxy true)
            (assoc-in [:security :hsts] false)              ;; TODO: YES! But this will break stuff.
            (assoc-in [:security :ssl-redirect] false)      ;; TODO: YES!
            (merge-config config))))

