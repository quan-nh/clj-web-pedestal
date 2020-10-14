add deps
```clj
[metosin/muuntaja "0.6.7"]
```

add `format-interceptor` to common list
```clj
(def common-interceptors [(format-interceptor) (body-params/body-params) http/html-body])
```

send request
```sh
$ curl http://localhost:8080/db
{"RESULT":15}

$ curl -H 'Accept:application/edn' http://localhost:8080/db
{:RESULT 15}
```
