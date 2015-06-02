(ns de.otto.tesla.stateful.serving
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :as jetty]
            [de.otto.tesla.stateful.handler :as handler]
            [clojure.tools.logging :as log]
            [de.otto.tesla.stateful.configuring :as config]))

;; The serving component is the frontend of the system.
;; It accepts requests and returns the data to be used by consuming systems.
;; For the moment a simple, blocking implementation with an embedded jetty is chosen.
(defrecord Server [config routes]
  component/Lifecycle
  (start [self]
    (log/info "-> starting server")
    (let [port (Integer. (config/config config [:server-port]))
          all-routes (handler/handler routes)
          server (jetty/run-jetty all-routes {:port port :join? false})]
      (.setGracefulShutdown server 100)
      (assoc self :server server)))

  (stop [self]
    (log/info "<- stopping server")
    (.stop (:server self))
    self))

(defn new-server [] (map->Server {}))
