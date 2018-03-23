(ns process-wrapper.main
  (:require
     [clojure.data.json :as json]
     [process-wrapper.server :refer :all])
  (:gen-class))

(defn -main [& args]
  (println "server starting...")
  (server
    (or (first args) 3001)
    json/write-str))
