(ns gorillalabs.tesla.component.amqp
  (:require [langohr.basic :as lb]
            [langohr.channel :as lch]
            [langohr.consumers :as lc]
            [langohr.core :as rmq]
            [langohr.exchange :as lx]
            [langohr.queue :as lq]
            [mount.core :as mnt]
            [gorillalabs.tesla.component.configuration :as config]
            [taoensso.nippy :as nippy]
            [clojure.tools.logging :as log]
            [clojure.spec :as spec]))

(defn- declare-queues! [conn]
  (let [ch (lch/open conn)
        queues (config/config config/configuration [:amqp :initial-queues])
        fanout-exchanges (config/config config/configuration [:amqp :fanout-exchanges])
        direct-exchanges (reduce (fn [r v] (lq/declare ch (first v) {:exclusive false :auto-delete false :durable true})
                                   (conj r (last v)))
                                 #{} queues)]
    (doseq [exchange direct-exchanges] (log/debugf "registering direct exchange %s." exchange) (lx/direct ch exchange {:durable true}))
    (doseq [[queue exchange] queues] (lq/bind ch queue exchange {:routing-key queue}))
    (doseq [exchange fanout-exchanges] (log/debugf "registering fanout %s." exchange) (lx/fanout ch exchange {:durable true}))
    (lch/close ch)))

(defn- start []
  (let [cfg (config/config config/configuration [:amqp :rabbit-mq])
        connection (if cfg (rmq/connect cfg) (rmq/connect))]
    (declare-queues! connection)
    (atom {:connection connection
           :channels   []})))

(defn- stop [state]
  (doseq [ch (:chanels @state)]
    (lch/close ch))
  (rmq/close (:connection @state)))

(mnt/defstate broker
              :start (start)
              :stop (stop broker))

(defn publish-text [exchange queue payload]
  "Publishes the supplied string payload to the queue on the supplied exchange with the supplied name."
  (assert (string? payload))
  (let [ch (lch/open (:connection @broker))
        queue-name (if (keyword? queue) (name queue) queue)]
    (lb/publish ch exchange queue-name payload {:content-type "text/plain" :type "default"})
    (lch/close ch)))

(defn publish-data [exchange queue payload]
  "Publishes the supplied string payload to the queue on the supplied exchange with the supplied name."
  (let [ch (lch/open (:connection @broker))
        queue-name (if (keyword? queue) (name queue) queue)
        serialized-payload (nippy/freeze payload)]
    (lb/publish ch exchange queue-name serialized-payload {:content-type "object/data" :type "default"})
    (lch/close ch)))

(defn- wrap-with-simplifier [f conform-spec]
  "Wraps the supplied handler function with ack-unless-exception, conversion of payload to content-type type (if known) and 
simplified arguments."
  (lc/ack-unless-exception
    (fn [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
      (let [converted-payload (case content-type
                                "text/plain" (String. payload)
                                "object/data" (nippy/thaw payload)
                                payload)]
        (when conform-spec (spec/assert conform-spec converted-payload))
        (f converted-payload)))))

(defn subscribe [queue-name handler & [conform-spec]]
  "Subscribes the supplied handler function to the queue with the supplied name. 
Handler function is called with event-name and payload (converted according to content-type of the message)."
  (let [ch (lch/open (:connection @broker))]
    (swap! broker update-in [:connections] conj ch)
    (lc/subscribe ch queue-name (wrap-with-simplifier handler conform-spec))))




















