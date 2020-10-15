(ns clj-web-pedestal.routes
  (:require [ring.util.response :as ring-resp]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [next.jdbc :as jdbc]
            [muuntaja.interceptor :refer [format-interceptor]]
            [io.pedestal.log :as log]
            [io.pedestal.interceptor.error :refer [error-dispatch]]))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn view-users [{{:keys [user-id]} :path-params}]
  (ring-resp/response (str "Hello " user-id)))

(defn db-page [{:keys [ds] :as req}]
  (log/debug :req req)
  (ring-resp/response (jdbc/execute-one! ds ["SELECT 3*5 AS result"])))

(defn bad-page
  [request]
  (ring-resp/response (str "Bad division: " (/ 3 0))))

(def error-handler
  (error-dispatch [ctx ex]

                  [{:exception-type :java.lang.ArithmeticException}]
                  (assoc ctx :response {:status 400 :body "A bad one"})

                  :else
                  (assoc ctx :io.pedestal.interceptor.chain/error ex)))

(def common-interceptors [error-handler (format-interceptor) (body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/users/:user-id" :get (conj common-interceptors `view-users)]
              ["/db" :get (conj common-interceptors `db-page)]
              ["/div" :get (conj common-interceptors `bad-page)]})
