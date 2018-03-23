(ns process-wrapper.core
  (:require
    [clojure.test :refer :all]
    [puget.printer :refer [cprint]]
    [clojure.inspector :as inspect :refer [inspect-tree]]
    [process-wrapper.util :refer :all]
    [process-wrapper.parse :refer :all]
    [process-wrapper.process-worker :refer :all]
    [me.raynes.conch.low-level :as sh]
    [instaparse.core :as insta]))

(def get-prediction-locking (Object.))
(defn get-prediction [worker input format-converter]
  " pushes input text on stdin, gets the classification for it on stdout.
    TODO: using a queue + an agent or core.async to avoid locking won't help, executor access should be serialized.
               we can manage spawning multiple workers, if their task doesn't blow up memory "
  (locking get-prediction-locking ; serialize access to this function because the spawned executor's stdin is not thread-safe

    ; push the input (should end with a newline)
    (sh/feed-from-string (get-in (val (first worker)) [:shell-object]) input)

    ; get the output prediction
    (let
      [rawparse-or-error
        (loop [raw-response (str)]
          (let
            [byte-read (.read (get-in (val (first worker)) [:shell-object :out]))]
              (assert (not= byte-read -1))
              (let [as-char (char byte-read)]
                (if (= byte-read 10) ; line-feed
                  raw-response
                  (recur (str raw-response as-char))))))

      parse (fasttext-output-parse rawparse-or-error)]

      parse)))
