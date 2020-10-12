(ns clj-web-pedestal.routes
  (:require [ring.util.response :as ring-resp]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn view-users [{{:keys [user-id]} :path-params}]
  (ring-resp/response (str "Hello " user-id)))

(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/users/:user-id" :get `view-users]})
