(ns gorillalabs.tesla.component.appstate
  (:require [mount.core :as mnt]
            [de.otto.status :as s]
            [clojure.data.json :as json :only [write-str]]
            [clojure.tools.logging :as log]
            [clojure.string :as str]
            [clj-time.local :as local-time]
            [metrics.timers :as timers]
            [gorillalabs.tesla.component.configuration :as config]
            [gorillalabs.tesla.component.handler :as handler]
            [ring.middleware.defaults :as ring-defaults]))

(declare appstate)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; internal helper functions

(defn- deconj [seq item]
  (filterv (partial not= item) seq))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; functions to compile an aggregated appstate



(defmulti aggregation-strategy
          "Selects the status aggregation strategy based upon config."
          (fn [config]
            (keyword (config/config config [:appstate :aggregation]))))

(defmethod aggregation-strategy :forgiving [_]
  s/forgiving-strategy)

(defmethod aggregation-strategy :default [_]
  s/strict-strategy)


(defn- keyword-to-state [kw]
  (str/upper-case (name kw)))

(defn- sanitize-str [s]
  (apply str (repeat (count s) "*")))

(defn- sanitize-mapentry [checklist [k v]]
  {k (if (some true? (map #(.contains (name k) %) checklist))
       (sanitize-str v)
       v)})

(defn- sanitize [config checklist]
  (into {}
        (map (partial sanitize-mapentry checklist) config)))


(defn- system-infos [config]
  {:systemTime (local-time/format-local-time (local-time/local-now) :date-time-no-ms)
   :hostname   (config/external-hostname config)
   :port       (config/external-port config)})




(defn current-state [appstate config]
  (let [extra-info {:name          (config/config config [:name])
                    :version       (config/config config [:version])
                    :git           (config/config config [:commit])
                    :configuration (sanitize (config/config config) ["pwd" "passwd"])}]
    (assoc
      (s/aggregate-status :application
                          (aggregation-strategy config)
                          @appstate
                          extra-info)
      :system (system-infos config))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; http related stuff

(defn- to-json [compiled-state]
  (into {} (map
             (fn [[k v]]
               {k (update-in v [:status] keyword-to-state)})
             compiled-state)))

(defn- response-body [self config]
  (-> (current-state self config)
      (update-in [:application :statusDetails] to-json)
      (update-in [:application :status] keyword-to-state)))

(defn- response [self config]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str (response-body self config))})

(defn- handle [appstate config request]
  (response appstate config))


(defn- appstate-route [config]
  (config/config config [:appstate :path] "state"))

(defn- create-handler [appstate config]
  (handler/wrap-site (partial handle appstate config)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; lifecycle functions

(defn- create-appstate []
  (atom []))



(defn- start []
  (log/info "-> start appstate")
  (let [;; the (new) state of this component
        appstate (create-appstate)

        ;; other components
        config config/configuration
        handler handler/handler
        ]
    (handler/register
      handler
      (appstate-route config)
      (create-handler appstate config))
    appstate))

(defn- stop [self]
  (log/info "<- Stopping appstate")
  (handler/deregister
    handler/handler
    (appstate-route config/configuration))
  self)

(mnt/defstate ^{:on-reload :noop}
              appstate
              :start (start)
              :stop (stop appstate))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; API to this component

(defn register-state-fn [appstate state-fn]
  (swap! appstate conj state-fn)
  appstate)


(defn deregister-state-fn [appstate state-fn]
  (swap! appstate deconj state-fn)
  appstate)
