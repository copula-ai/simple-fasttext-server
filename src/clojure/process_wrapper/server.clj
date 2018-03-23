(ns process-wrapper.server
  (:require
     [yada.yada :refer [listener resource as-resource]]
     [clojure.data.json :as json]
     [process-wrapper.process-worker :refer :all]
     [process-wrapper.core :refer :all]))

(defn server [port format-converter]
  " http only for now. https support entails a bit / much more work ― 
    https://github.com/SevereOverfl0w/aleph-yada-ssl/blob/master/src/jkdjka/yada.clj "
  (let
    [worker (attach ["fasttext/fastText/fasttext" "predict-prob" "fasttext/classifier.bin" "-" "1000"])
     classify
       (fn [ctx]
          (let [text (get-in ctx [:parameters :query :text])]
             #_(println "predicting for" text)
             (get-prediction worker (str text "\n")  format-converter)))]

    (def yada-server
      (listener
         ["/"

           [["status"
               (resource
                  {:produces "text/plain"
                    :response "Server is up"})]

            ["predict"
               (resource
                 {:methods
                    {:get
                      {:parameters {:query {:text String}}
                        :produces "application/json"
                        :response classify}}})]
            [true (as-resource nil)]]]

       {:port port})))

  (println "listening on port" port)
  (future @(promise))) ;; block forever to keep the yada server alive
