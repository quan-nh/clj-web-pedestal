(ns clj-web-pedestal.system
  (:gen-class)                                              ; for -main method in uberjar
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [clj-web-pedestal.server :as server]
            [clj-web-pedestal.routes :as routes]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(def ^:private db-spec {:dbtype "h2" :dbname "example" :username "sa" :password ""})

(defn new-system
  [env]
  (-> (component/system-map
        :db (connection/component HikariDataSource db-spec)
        :service-map {:env          env
                      ::http/routes routes/routes
                      ::http/type   :jetty
                      ::http/port   8080
                      ::http/join?  false}
        :server (server/new-server))

      (component/system-using
        {:server [:service-map :db]})))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (component/start (new-system :prod)))
