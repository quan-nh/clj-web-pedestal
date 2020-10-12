Pedestal supports different routing syntax:
- Terse/Vector-based routes
- Verbose/Map-based routes
- Tabular routes (current prefer)

Table syntax is more wordy and has some repetition from row to row, but it also has some advantages:
- The parser is simpler and produces better error messages when the input is not right. (This includes some work to make stack traces more helpful.)
- The table does not have hierarchic nesting, so the rows are independent.
- The input is just data, so you can read it from an EDN file or compose it with regular functions. No more syntax-quoting to create interceptors with parameters.

The simplest route has just a URL, an HTTP verb, and a handler:

    ["/users" :get `view-users]

## Params
### Path Parameters

    ["/users/:user-id" :get `view-users]

When the request reaches our view-users handler, it will include a map like this:

    {:path-params {:user-id "miken"}}

with constraints

    ["/user/:user-id" :get `view-user :constraints {:user-id #"[0-9]+"}]

### Query Parameters

For example, an HTTP request with for the URL:

    /search?q=blog

will have this in the request map:

    {:query-params {:q "blog"}}

## Interceptors

The route table allows you to put a vector of interceptors (or things that resolve to interceptors) in that third position.

    ["/user/:user-id/private" :post [inject-connection auth-required (body-params/body-params) view-user]]

Common interceptors

    ;; Make a var with the common stuff
    (def common-interceptors [inject-connection auth-required (body-params/body-params)])

    ;; inside a call to table-routes
    ["/user/:user-id/private" :post (conj common-interceptors view-user)]


