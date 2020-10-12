(ns clj-web-pedestal.system
  (:gen-class)                                              ; for -main method in uberjar
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [clj-web-pedestal.server :as server]
            [clj-web-pedestal.routes :as routes]))

(defn new-system
  [env]
  (component/system-map
    :service-map
    {:env          env
     ::http/routes routes/routes
     ::http/type   :jetty
     ::http/port   8080
     ::http/join?  false}

    :server
    (component/using
      (server/new-server)
      [:service-map])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (component/start (new-system :prod)))
