(ns gorillalabs.tesla.http.httpkit
    (:require [bidi.ring :as br]
      [org.httpkit.server :as httpkit]
      [clojure.tools.logging :as log]
      [gorillalabs.tesla.component.configuration :as config]
      ))

(defmacro condasit->
          "A mixture of cond-> and as-> allowing more flexibility in the test and step forms,
          also binding `it` to the result of the cond predicate."
          [expr name & clauses]
          (assert (even? (count clauses)))
          (let [pstep (fn [[test step]] `(if-let [~'it ~test] ~step ~name))]
               `(let [~name ~expr
                      ~@(interleave (repeat name) (map pstep (partition 2 clauses)))]
                     ~name)))

(def default-port 3000)

(defn server-config [config]
      (condasit-> {:port (get-in config [:httpkit :port] default-port)
                   :ip   (get-in config [:httpkit :bind] "0.0.0.0")}
                  server-conf

                  (get-in config [:httpkit :thread])
                  (assoc server-conf :thread it)

                  (get-in config [:httpkit :queue-size])
                  (assoc server-conf :queue-size it)

                  (get-in config [:httpkit :max-body])
                  (assoc server-conf :max-body it)

                  (get-in config [:httpkit :max-line])
                  (assoc server-conf :max-line it)))


(defn start [handler]
      (log/info "-> starting httpkit")
      (let [server-config (server-config config/configuration)]
           (log/info "Starting httpkit with port" (server-config :port) "and bind" (server-config :ip))
           (httpkit/run-server handler server-config)))

(defn stop [server]
      (let [timeout (config/config config/configuration [:httpkit-timeout] 100)]
           (if server
             (do
               (log/info "<- stopping httpkit with timeout:" timeout "ms")
               (server :timeout timeout))
             (log/info "<- stopping httpkit"))))
