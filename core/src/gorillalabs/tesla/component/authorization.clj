(ns gorillalabs.tesla.component.authorization
  (:require [mount.core :as mnt]
            [clojure.tools.logging :as log]
            [gorillalabs.tesla.component.configuration :as config]
            [buddy.sign.jwt :as jwt]
            [clj-time.core :refer [hours from-now]]
            [buddy.auth.backends.token :as auth]
            [buddy.core.hash :as hash]
            ))


(declare authorization)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; http related stuff

(defn- unauthorized [& _] {:status 403 :body {:message "Unauthorized. Please login first."}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; internal helper functions

(defn- get-config [key config]
  (config/config config [:authorization key]))

(defn- secret [config]
  (let [secret? (config/config config [:authorization :secret])]
    (when secret? (hash/sha256 secret?))))

(def options
  (partial get-config :options))

(def authdata
  (partial get-config :authdata))

(defn- get-state [config]
  {:secret               (secret config)
   :options              (options config)
   :authdata             (authdata config)
   :unauthorized-handler unauthorized})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; API to this component

(defn authorize [username password]
  (let [valid? (some-> (:authdata authorization)
                       (get-in [(keyword username) :password])
                       (= password))]
    (when (and username password)
      (if valid?
        (let [claims {:user (keyword username)
                      :exp  (-> 3 hours from-now)}]
          (jwt/encrypt claims (:secret authorization) (:options authorization)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; lifecycle functions

(defn- create-authorization []
  (let [config config/configuration
        state (get-state config)]
    (assoc state :backend (auth/jwe-backend state))))

(defn- start []
  (log/info "-> Starting authorization.")
  (create-authorization))

(defn- stop [self]
  (log/info "<- Stopping authorization")
  self)

(mnt/defstate authorization
              :start (start)
              :stop (stop authorization))
