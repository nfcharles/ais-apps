(ns apps.decode
  (:require [apps.runner :as apps-runner]
            [taoensso.timbre :as logging])
  (:gen-class))

(defn -main
  [& args]
  (try
      (time (apply apps-runner/run args))
    (catch Exception e
      (logging/fatal e))))
