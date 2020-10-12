(ns user
  (:require
    [clj-web-pedestal.system :refer [new-system]]
    [com.stuartsierra.component.repl
     :refer [reset set-init start stop system]]))

(set-init (constantly (new-system :dev)))
