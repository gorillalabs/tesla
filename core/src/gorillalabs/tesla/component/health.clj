(ns gorillalabs.tesla.component.health
    (:require [mount.core :as mnt]
      [clojure.tools.logging :as log]
      [gorillalabs.tesla.component.configuration :as config]
      ))


(declare health)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; http related stuff

;; http response for a healthy system
(def healthy-response {:status  200
                       :headers {"Content-Type" "text/plain"}
                       :body    "HEALTHY"})
;; http response for an unhealthy system
(def unhealthy-response {:status  503
                         :headers {"Content-Type" "text/plain"}
                         :body    "UNHEALTHY"})

(defn- health-response [healthy?]
       (if @healthy?
         healthy-response
         unhealthy-response))


(defn route [config]
      (config/config config/configuration [:health :path] "/health"))

(defn handler [request]
      (health-response health))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; lifecycle functions

(defn- start []
       (log/info "-> starting healthcheck.")
       (let [healthy? (atom true)]
            healthy?))

(defn- stop [self]
       (log/info "<- stopping Healthcheck")
       self)

(mnt/defstate health
              :start (start)
              :stop (stop health))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; API to this component

(defn healthy [health]
      (reset! health true))

(defn unhealthy [health]
      (reset! health false))





