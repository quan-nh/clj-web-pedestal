(ns clj-web-pedestal.server
  (:require [io.pedestal.http :as http]
            [com.stuartsierra.component :as component]
            [io.pedestal.interceptor :refer [interceptor]]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defn db-interceptor [db]
  (interceptor
    {:name ::db-interceptor
     :enter
           (fn [context]
             (update context :request assoc :db db))}))

(defrecord Server [service-map db server]
  component/Lifecycle
  (start [this]
    (if server
      this
      (let [runnable-service (-> service-map
                                 http/default-interceptors
                                 (update ::http/interceptors conj (db-interceptor db))
                                 http/create-server)]
        (assoc this :server (if (test? service-map)
                              runnable-service
                              (http/start runnable-service))))))

  (stop [this]
    (when (and server (not (test? service-map)))
      (http/stop server))
    (assoc this :server nil)))

(defn new-server [] (map->Server {}))
