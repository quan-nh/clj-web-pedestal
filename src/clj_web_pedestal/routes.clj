(ns clj-web-pedestal.routes
  (:require [ring.util.response :as ring-resp]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [next.jdbc :as jdbc]))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn view-users [{{:keys [user-id]} :path-params}]
  (ring-resp/response (str "Hello " user-id)))

(defn db-page [{:keys [db]}]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (jdbc/execute-one! (db) ["SELECT 3*5 AS result"])})

(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/users/:user-id" :get `view-users]
              ["/db" :get `db-page]})
