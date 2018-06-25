(defproject ais-apps "0.3.0-SNAPSHOT"
  :description "AIS (Automatic Identification System) decoding application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"local" "file:maven_repository"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ais "0.9.2"]
                 [com.taoensso/timbre "4.3.1"]
                 [gorillalabs/sparkling "2.1.2"]
                 [org.apache.spark/spark-core_2.11 "2.1.2"]
                 [org.apache.spark/spark-sql_2.11 "2.1.1"]
                 [org.apache.hadoop/hadoop-aws "2.8.3"]
                 [com.fasterxml.jackson.core/jackson-core "2.6.5"]
                 [com.fasterxml.jackson.core/jackson-annotations "2.6.5"]
                 [org.clojure/data.json "0.2.6"]]
  :aot :all
  :main apps.spark.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
