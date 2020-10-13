(ns clj-web-pedestal.server
  (:require [io.pedestal.http :as http]
            [com.stuartsierra.component :as component]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defrecord Server [service-map server]
  component/Lifecycle
  (start [this]
    (if server
      this
      (cond-> service-map
              true http/create-server
              (not (test? service-map)) http/start
              true ((partial assoc this :server)))))

  (stop [this]
    (when (and server (not (test? service-map)))
      (http/stop server))
    (assoc this :server nil)))

(defn new-server [] (map->Server {}))
