(ns clj-web-pedestal.system
  (:gen-class)                                              ; for -main method in uberjar
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [clj-web-pedestal.config :refer [config]]
            [clj-web-pedestal.db :as db]
            [clj-web-pedestal.server :as server]
            [clj-web-pedestal.routes :as routes]))

(defn new-system
  [env]
  (-> (component/system-map
        :config config
        :db (db/new-db)
        :service-map {:env                  env
                      ::http/routes         routes/routes
                      ::http/resource-path  "/public"
                      ::http/secure-headers {:content-security-policy-settings {:object-src "none"}}
                      ::http/type           :jetty
                      ::http/port           8080
                      ::http/join?          false}
        :server (server/new-server))

      (component/system-using
        {:db     [:config]
         :server [:service-map :db]})))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (component/start (new-system :prod)))
