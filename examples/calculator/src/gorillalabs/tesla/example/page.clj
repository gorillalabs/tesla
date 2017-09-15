(ns gorillalabs.tesla.example.page
  (:require
    [de.otto.status :as status]
    [gorillalabs.tesla.example.calculating :as calculating]
    [gorillalabs.tesla.component.appstate :as appstate]
    [hiccup.core :as hiccup]
    [mount.core :as mnt]
    [ring.util.response :as response]
    [clojure.tools.logging :as log]
    [gorillalabs.tesla.component.configuration :as config]))

(declare page)


(defn wrap-page [body]
  (-> body
      response/response
      (response/content-type "text/html")))


(defn usage [_]
  (let [x (calculating/calculations calculating/calculator)]
    (wrap-page (hiccup/html [:body [:h1 "TO UPPER CASE"]
                             [:div (str "call /example/foo to get FOO")]
                             [:div (str @x " calculations so far")]]))))

(defn result [{:keys [route-params]}]
  (let [input (:input route-params)
        result (calculating/calculate! calculating/calculator input)]
    (wrap-page (hiccup/html [:body [:h1 "TO UPPER CASE"]
                             [:div (str input " to upper case is " result)]]))))


(defn- start []
  (appstate/register-state-fn
    appstate/appstate
    (fn [] (status/status-detail :example-page :ok "page is always fine")))
  (log/info "-> Starting page")
  true)

(defn- stop [self]
  (log/info "<- Stopping page.")
  false)

(mnt/defstate page
              :start (start)
              :stop (stop page))

