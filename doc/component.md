### Component

add deps
```clj
[com.stuartsierra/component "1.0.0"]
```

server.clj
```clj
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
```

move routes to `routes.clj`

system.clj
```clj
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
```

`lein run`

### Reloadable workflow

add dev profile
```clj
:profiles {:dev     {:source-paths ["dev"]
                     :dependencies [[com.stuartsierra/component.repl "0.2.0"]]}
           ..}
:repl-options {:init-ns user}
```

`dev/user.clj`
```clj
(ns user
  (:require
    [clj-web-pedestal.system :refer [new-system]]
    [com.stuartsierra.component.repl
     :refer [reset set-init start stop system]]))

(set-init (constantly (new-system :dev)))
```

`lein repl`
 
You can now manipulate the system in the REPL: (start), (stop) & (reset)

Change the code & `(reset)` to see if it affects!
