add deps
```clj
[seancorfield/next.jdbc "1.1.588"]
[com.h2database/h2 "1.4.199"]
[com.zaxxer/HikariCP "3.4.5"]
```

`next.jdbc` has support for Component built-in, via the next.jdbc.connection/component function
 which creates a Component-compatible entity which you can start and then invoke as a function with no arguments to obtain the DataSource within.

`system.clj`
```clj
defn new-system
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
```

we create an interceptor to add `db` to request context
```clj
(defn db-interceptor [db]
  (interceptor/interceptor
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
..
```

then on a handler
```clj
(defn db-page [{:keys [db]}]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (jdbc/execute-one! (db) ["SELECT 3*5 AS result"])})
```
