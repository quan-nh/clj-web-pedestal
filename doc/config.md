add deps
```clj
[aero "1.1.6"]
```

move `db-spec` to config file `resources\config.edn`
```clj
{:db-spec
 {:dbtype   "h2"
  :dbname   "example"
  :username "sa"
  :password ""}}
```

`config.clj` component
```clj
(ns clj-web-pedestal.config
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]
            [aero.core :refer [read-config]]))

(def config (with-meta {} {`component/start (fn [_] (read-config (io/resource "config.edn")))
                           `component/stop  (fn [_] (constantly "stopped"))}))
```

`db.clj` component
```clj
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
```

update `system.clj` to add config component
```clj
(defn new-system
  [env]
  (-> (component/system-map
        :config config
        :db (db/new-db)
        :service-map {:env          env
                      ::http/routes routes/routes
                      ::http/type   :jetty
                      ::http/port   8080
                      ::http/join?  false}
        :server (server/new-server))

      (component/system-using
        {:db     [:config]
         :server [:service-map :db]})))
```
