(ns apps.spark.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [sparkling.conf :as conf]
            [sparkling.core :as spark]
            [sparkling.destructuring :as s-de]
            [sparkling.serialization]
            [apps.spark.configuration :as apps-config]
            [ais.core :as ais-core]
            [ais.extractors :as ais-ex]
            [clojure.data.json :as json])
  (:import [scala Tuple2]
           [org.apache.spark.api.java Optional]
	   [org.apache.spark.api.java JavaSparkContext]
	   [com.amazonaws.auth DefaultAWSCredentialsProviderChain]	   
           [com.amazonaws.services.s3 AmazonS3 AmazonS3Client]
           [com.amazonaws.services.s3.model ListObjectsRequest
                                           ; ListObjectsV2Request
                                           ; ListObjectsV2Result
                                            ObjectListing
                                            S3ObjectSummary])
	   ;[com.amazonaws.services.s3 S3CredentialsProviderChain])
  (:gen-class))



(def delimiter #"[\s]+")

;; --------------
;; -    UTIL    -
;; --------------

(defn s3->rdd [sc & files]
  (println (format "FILES=%s"files))
  (loop [xs files
         acc []]
    (if-let [f (first xs)]
      (let [rdd (spark/text-file sc f)]
        (println rdd)
	(recur (rest xs) (conj acc rdd)))
      acc)))

(defn local->rdd [sc & files]
  (let [source (partial format "/tmp/%s")]
    (loop [xs files
           acc []]
      (if-let [f (first xs)]
        (let [rdd (spark/parallelize sc (source f))]
          (println rdd)
	  (recur (rest xs) (conj acc rdd)))
        acc))))

(defn load->rdd [sc & files]
  (loop [xs files
         acc []]
    (if-let [f (first xs)]
      (let [rdd (spark/text-file sc f)]
        (println rdd)
        (recur (rest xs) (conj acc rdd)))
      acc)))

(defn get-sources [path]
  (rest (map #(.getPath %) (->> path io/file file-seq))))

;; ----------------
;; -   APP LOGIC  -
;; ----------------

(defn tokenize [msgs]
  #_(println (format "Tokenizing=%s" msgs))
  (loop [xs msgs
         acc []]
    (if-let [msg (first xs)]
      (recur (rest xs) (conj acc (ais-ex/tokenize msg)))
      acc)))

(defn preprocess [line]
  (if (string/includes? line "|")
    (string/split line #"\|")
    [line]))

(defn preprocess [line & {:keys [delim]
                          :or {delim delimiter}}]
  (if (re-find delim line)
    (string/split line delim)
    [line]))


(defn printthru [label x]
  (if (nil? x)
    (println (format "DEBUG[%s]: <<<<<<<<<< NIL >>>>>>>>>>" label))
    (do
      (println (format "DEBUG[%s]: (%s)" label x))
      x)))

(defn process [delim line]
  (->> (preprocess line :delim delim)
       (tokenize)
       #_(printthru "After 'tokenize'")
       (ais-core/parse "json")
       (json/write-str)))

#_(defn process [line]
  (->> (preprocess line)
       (count)))

(defn decode-ais [sc delim rdds]
  (let [-process (partial process delim)]
    (loop [xs rdds
           acc []]
      (if-let [rdd (first xs)]
        (recur (rest xs) (conj acc (spark/map -process rdd)))
        acc))))

(defn run [sc delim output sources]
  (let [d (re-pattern delim)
        rdds (->> (apply load->rdd sc sources)
                  (decode-ais sc d))]
    (loop [xs rdds
           i 0]
      (if-let [rdd (first xs)]
        (let [out-path (format "%s-%d" output i)]
          (->> rdd
               (spark/save-as-text-file out-path))
          (recur (rest xs) (inc i)))))))

;; --------------
;; -    MAIN    -
;; --------------


(defn -main
  [& args]
  (let [sc    (apps-config/spark-context :master "local[2]")
        in    (apps-config/get-input)
        out   (apps-config/get-output)
        delim (apps-config/get-delimiter)]
    (println (format "INPUT_SOURCE=%s" in))
    (println (format "OUTPUT_DEST=%s" out))
    (println (format "DELIMITER=%s" delim))
    (run sc delim out (get-sources in))))
