(ns clj-web-pedestal.config
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]
            [aero.core :refer [read-config]]))

(def config (with-meta {} {`component/start (fn [_] (read-config (io/resource "config.edn")))
                           `component/stop  (fn [_] (constantly "stopped"))}))
