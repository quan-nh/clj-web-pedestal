new app:

    lein new pedestal-service clj-web-pedestal

the template generated two files under src/clj_web_pedestal:

    service.clj defines the logic of our service.
    server.clj creates a server (a daemon) to host that service.

run it in dev mode:

    lein repl
    clj-web-pedestal.server=> (server/start runnable-service)

Then hit Ctrl-C to get back to the REPL prompt.

Go to http://localhost:8080/ and you'll see a shiny "Hello World!" in your browser.

Done! Let's stop the server and exit the REPL.

    clj-web-pedestal.server=> (server/stop runnable-service)
    clj-web-pedestal.server=> (quit)
    Bye for now!
