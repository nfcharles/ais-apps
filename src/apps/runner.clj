(ns apps.runner
  (:require [ais.runner :as ais-runner]
            [clojure.core.async :as async]
            [taoensso.timbre :as logging])
  (:gen-class))

(def stdin-reader
  (java.io.BufferedReader. *in*))


(defn input-stream [reader]
  (async/to-chan (line-seq reader)))

(defn parse-output-prefix [args]
  (nth args 0))

(defn parse-decode-types [args]
  (ais-runner/parse-types (nth args 1)))

(defn parse-thread-count [args]
  (read-string (nth args 2)))

(defn parse-output-format [args]
  (nth args 3))

(defn parse-write-buffer-length [args]
  (read-string (nth args 4)))

(defn parse-filename [args]
  (nth args 5))

(defn -file-runner
  [& args]
  (try
    (ais-runner/configure-logging :std-err)
    (let [filename (parse-filename args)]
      (println (format "*** Decoding %s ***" filename))
      (with-open [rdr (clojure.java.io/reader (parse-filename args))]
        (time (ais-runner/run (input-stream rdr)
                              (parse-output-prefix args)
                              (parse-decode-types args)
                              (parse-thread-count args)
                              (parse-output-format args)
                              (parse-write-buffer-length args)))))
    (catch Exception e
      (logging/fatal e))))

(defn -stdin-runner
  [& args]
  (try
    (ais-runner/configure-logging :std-err)
    (time (ais-runner/run (input-stream stdin-reader)
                          (parse-output-prefix args)
                          (parse-decode-types args)
                          (parse-thread-count args)
                          (parse-output-format args)
                          (parse-write-buffer-length args)))
    (catch Exception e
      (logging/fatal e))))

(defn run [& args]
  (case (first args)
    "stdin" (apply -stdin-runner (rest args))
    "file"  (apply -file-runner (rest args))
    (throw (java.lang.Exception "Invalid input stream type."))))
