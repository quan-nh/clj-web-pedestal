(defproject clj-web-pedestal "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.8" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [com.stuartsierra/component "1.0.0"]
                 [seancorfield/next.jdbc "1.1.588"]
                 [com.h2database/h2 "1.4.199"]
                 [com.zaxxer/HikariCP "3.4.5"]
                 [aero "1.1.6"]
                 [metosin/muuntaja "0.6.7"]

                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 [io.pedestal/pedestal.jetty "0.5.8"]
                 ;; [io.pedestal/pedestal.immutant "0.5.8"]
                 ;; [io.pedestal/pedestal.tomcat "0.5.8"]

                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  ;:java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.5"]]
  :profiles {:dev     {:source-paths ["dev"]
                       :dependencies [[com.stuartsierra/component.repl "0.2.0"]]}
             :uberjar {:aot [clj-web-pedestal.system]}}
  :repl-options {:init-ns user}
  :global-vars {*warn-on-reflection* true}
  :main ^{:skip-aot true} clj-web-pedestal.system)
