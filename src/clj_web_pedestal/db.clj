(ns clj-web-pedestal.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defrecord Database [^HikariDataSource ds config]
  component/Lifecycle
  (start [this]
    (assoc this :ds (connection/->pool HikariDataSource (:db-spec config))))

  (stop [this]
    (.close ds)
    (assoc this :ds nil)))

(defn new-db [] (map->Database {}))
