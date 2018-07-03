(ns apps.spark.configuration
  (:require [clojure.string :as string]
            [sparkling.conf :as conf]
            [sparkling.core :as spark]
            [sparkling.destructuring :as s-de]
            [sparkling.serialization])
  (:import [scala Tuple2]
           [org.apache.spark.api.java Optional]
	   [org.apache.spark.api.java JavaSparkContext])	    
  (:gen-class))



(defn get-env [k]
  (System/getenv k))

(defn get-input []
  (get-env "AIS_INPUT_SOURCE"))

(defn get-output []
  (get-env "AIS_OUTPUT_DEST"))

(defn get-delimiter []
  (get-env "AIS_MULTIPART_DELIMITER_REGEX"))

(defn set-hadoop [ctx k v]
  (println (format "setting key[%s]" k))
  (.set (.hadoopConfiguration ctx) k v))

(defn configure-common [ctx]
  (set-hadoop ctx "fs.s3a.access.key" (get-env "AWS_ACCESS_KEY_ID"))
  (set-hadoop ctx "fs.s3a.secret.key" (get-env "AWS_SECRET_ACCESS_KEY")))  

(defn configure-from-role [ctx]
  (configure-common ctx)
  (set-hadoop ctx "fs.s3a.aws.credentials.provider" "org.apache.hadoop.fs.s3a.TemporaryAWSCredentialsProvider")
  (set-hadoop ctx "fs.s3a.session.token" (get-env "AWS_SESSION_TOKEN")))

(defn configure [ctx]
  (configure-common ctx))

(defn ^JavaSparkContext spark-context [& {:keys [app-name master]
                                          :or {app-name "ais-decoder"
                                               master   "local[*]"}}]
  (println (format "SPARK_MASTER=%s" master))
  (println (format "SPARK_APP_NAME=%s" app-name))
  (let [ctx (-> (conf/spark-conf)
                (conf/master master)
                (conf/app-name app-name)
                spark/spark-context)]
    (if (= "role" (get-env "AUTH_TYPE"))
      (configure-from-role ctx)
      (configure ctx))
    ctx))
