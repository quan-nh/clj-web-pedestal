(ns clj-web-pedestal.routes
  (:require [ring.util.response :as ring-resp]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [next.jdbc :as jdbc]
            [muuntaja.interceptor :refer [format-interceptor]]
            [io.pedestal.log :as log]))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn view-users [{{:keys [user-id]} :path-params}]
  (ring-resp/response (str "Hello " user-id)))

(defn db-page [{:keys [ds] :as req}]
  (log/debug :req req)
  (ring-resp/response (jdbc/execute-one! ds ["SELECT 3*5 AS result"])))

(def common-interceptors [(format-interceptor) (body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/users/:user-id" :get (conj common-interceptors `view-users)]
              ["/db" :get (conj common-interceptors `db-page)]})
