(defproject ais-apps "0.1.0-SNAPSHOT"
  :description "AIS (Automatic Identification System) decoding application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"local" "file:maven_repository"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [ais "0.8.3"]
                 [com.taoensso/timbre "4.3.1"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :main apps.decoding
  :aot [apps.decoding])
