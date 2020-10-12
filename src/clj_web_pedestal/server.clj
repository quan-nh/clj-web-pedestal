(ns clj-web-pedestal.server
  (:require [io.pedestal.http :as http]
            [com.stuartsierra.component :as component]))

(defrecord Server [service-map server]
  component/Lifecycle
  (start [this]
    (if server
      this
      (assoc this :server (-> service-map
                              http/create-server
                              http/start))))

  (stop [this]
    (when server (http/stop server))
    (assoc this :server nil)))

(defn new-server [] (map->Server {}))
