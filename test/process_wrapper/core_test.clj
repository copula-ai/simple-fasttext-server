(ns process-wrapper.core-test
  (:require
    [puget.printer :refer [cprint]]
    [clojure.inspector :as inspect :refer [inspect-tree]]
    [clojure.test :refer :all]
    [clj-http.client :as http-client]
    [process-wrapper.server :refer :all]))

(defn char-range [from to]
   (map char (range (int from) (inc (int to)))))

(defn random-utterance []
   (let [chars (char-range \א \ת)
          word-len (+ 5 (rand-int 5))
          words-in-utterance (+ 1 (rand-int 9))]
             (clojure.string/join " "
                (take words-in-utterance
                  (repeatedly
                      (fn [] (clojure.string/join
                         (take word-len
                            (repeatedly
                               (fn [] (rand-nth chars)))))))))))

(deftest end-to-end-performance
  (println "server starting for end-to-end tests...")
  (server 3333 identity)

  (println)

  " performance test "
  (println "sequentially making 1000 calls with random strings to self, to measure best warmed-up latency...")

  (let [requests 1000
         start-time (System/nanoTime)
         response-times
            (doall (map
              :request-time
              (map
                (fn [_] (http-client/get "http://localhost:3333/predict" {:query-params {"text" (random-utterance)}}))
                (range requests))))
         end-time (System/nanoTime)]

                    (println "measured response times in msec:")
                    (println "throughput:" (int (/ requests (/ (- end-time start-time) 1e9))) "responses per second"))

 (println)

  " concurrency and throughput test  "
  (println "making concurrent calls to loosely test for concurrency...")

  (let [requests 5000
         start-time (System/nanoTime)
         response-times
            (doall (map
              :request-time
              (pmap
                (fn [_]
                  (http-client/get "http://localhost:3333/predict" {:query-params {"text" (random-utterance)}}))
                (range requests))))
         end-time (System/nanoTime)]

                      (println "measured response times in msec:" response-times)
                      (println "throughput:" (int (/ requests (/ (- end-time start-time) 1e9))) "responses per second")))
