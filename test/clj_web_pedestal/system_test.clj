(ns clj-web-pedestal.system-test
  (:require [clojure.test :refer :all]
            [io.pedestal.http :as http]
            [io.pedestal.test :refer [response-for]]
            [clj-web-pedestal.system :as system]
            [com.stuartsierra.component :as component]))

(defn service-fn
  [system]
  (get-in system [:server :server ::http/service-fn]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(deftest home-page-test
  (with-system [sut (system/new-system :test)]
               (let [service (service-fn sut)
                     {:keys [status body headers]} (response-for service :get "/")]
                 (is (= 200 status))
                 (is (= "Hello World!" body))
                 (is (= {"Content-Type"                      "text/html;charset=UTF-8"
                         "Strict-Transport-Security"         "max-age=31536000; includeSubdomains"
                         "X-Frame-Options"                   "DENY"
                         "X-Content-Type-Options"            "nosniff"
                         "X-XSS-Protection"                  "1; mode=block"
                         "X-Download-Options"                "noopen"
                         "X-Permitted-Cross-Domain-Policies" "none"
                         "Content-Security-Policy"           "object-src 'none'; script-src 'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:;"}
                        headers)))))
