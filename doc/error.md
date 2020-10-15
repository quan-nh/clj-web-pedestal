add a bad-page
```clj
(defn bad-page
  [request]
  (ring-resp/response (str "Bad division: " (/ 3 0))))
```

Pedestal offers a macro `error-dispatch` to build error-handling interceptors that use pattern-matching `core.match` to select a clause.
```clj
(def service-error-handler
  (err/error-dispatch [ctx ex]
    ; Handle `ArithmeticException ex
    [{:exception-type :java.lang.ArithmeticException}]
    (assoc ctx :response {:status 400 :body "A bad one"})

    :else
    (assoc ctx :io.pedestal.interceptor.chain/error ex)))

;; update commont interceptors
(def common-interceptors [service-error-handler (body-params/body-params) http/html-body])
```

http://pedestal.io/cookbook/index#_how_to_handle_errors
