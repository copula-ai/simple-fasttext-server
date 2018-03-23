(ns process-wrapper.process-worker
    (:require
      [clojure.test :refer :all]
      [puget.printer :refer [cprint]]
      [clojure.inspector :as inspect :refer [inspect-tree]]
      [process-wrapper.util :refer :all]
      [me.raynes.conch.low-level :as sh]))

; workers management atom ― but we only spawn and manage a single worker for now
(def workers (atom {}))

; debug watch
#_(add-watch worker-status :watch
 (fn [watch-name watch-object old new]
   (println "from" (-> old vals first :status) "to" (-> new vals first :status))))

(defn attach [command-and-args]
  " starts and returns a process ready to work with "
  (let
     [conch-object (apply sh/proc command-and-args) ; this is the map returned by `conch/sh`, consisting of the keys: :err :in :out :process
      process-object (:process conch-object)                ; the java process object
      new-worker
        {process-object
          {:shell-object conch-object
           :status :idle}}]

     (swap! workers conj new-worker)

     new-worker))
