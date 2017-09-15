(ns gorillalabs.tesla.example
  (:require [gorillalabs.tesla.example.calculating :as calculating]
            [gorillalabs.tesla.example.page :as page]

            [clojure.tools.logging :as log]
            [gorillalabs.tesla :as tesla]
            [gorillalabs.tesla.component.configuration :as config]

            [gorillalabs.tesla.http.httpkit :as httpkit]
            [gorillalabs.tesla.http.handler :as handler]
            [gorillalabs.tesla.example.page :as page]
            [mount.core :as mnt])
  (:gen-class))


(declare example)

(defn create-handler []
  (handler/wrap-site
    (bidi.ring/make-handler ["/" [["example" (fn [req] (page/usage req))]
                                  [["example/" :input] (fn [req] (page/result req))]]])))


(defn- start []
  (log/info "-> starting example system.")
  (httpkit/start (create-handler)))

(defn- stop [httpkit]
  (log/info "<- stopping example system")
  (httpkit))

(mnt/defstate example
              :start (start)
              :stop (stop example))

(defn -main
  "starts up the production system."
  [& args]
  (tesla/start)
  )
