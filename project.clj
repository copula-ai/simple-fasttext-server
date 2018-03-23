(defproject process-wrapper "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"

  :source-paths      ["src/clojure"]
  :test-paths ["test" "src/clojure"] ; for picking up unit tests from regular source files not only the tests directory
  :javac-options ["-target" "1.8" "-source" "1.8"]

  :dependencies [[org.clojure/clojure "1.8.0"]

                 [me.raynes/conch "0.8.0"]         ; proper shelling out for clojure

                 ; yada
                 [yada "1.2.6"]
                 [aleph "0.4.1"]
                 [bidi "2.0.12"]

                 [instaparse "1.4.7"]

                 ;[com.taoensso/timbre "4.10.0"]   ; clojure logging

                 [org.slf4j/slf4j-simple "1.7.25"] ; for the draining of sl4fj-reliant libraries used here

                 [clj-time "0.13.0"]               ; https://github.com/clj-time/clj-time

                 [cheshire "5.7.1"]                ; for working with json

                 [org.clojure/data.csv "0.1.3"]    ; for csv

                 [org.clojure/math.combinatorics "0.1.4"]

                 [org.clojure/data.json "0.2.6"]

                 [clj-http "3.8.0"]

                 [io.aviso/pretty "0.1.33"] ; pretty exceptions in leinigen (http://ioavisopretty.readthedocs.io/en/latest/lein-plugin.html)
                 [mvxcvi/puget "1.0.1"]]    ; color printing function (https://github.com/greglook/puget#usage), see `with-color` and `cprint`

  :profiles {
    :uberjar {:aot :all}
    :dev {:dependencies
                    [[org.clojure/tools.trace "0.7.5"]
                     [proto-repl "0.3.1"]
                     [criterium "0.4.3"]
                     [rhizome "0.2.5"]
                     ;[org.noisesmith.poirot :as poirot]
                     ]}}

  :plugins [[io.aviso/pretty "0.1.33"]      ; pretty exceptions in leinigen, needed here as well as in :dependencies
            #_[mvxcvi/whidbey "1.3.1"]        ; more colorful repl (https://github.com/greglook/whidbey)
            [lein-codox "0.10.3"]
            [lein-auto "0.1.3"]]   ; provides the auto lein command for watching source changes

  :codox {:metadata {:doc/format :markdown}} ; treat docstrings as codox extended markdown (https://github.com/weavejester/codox/blob/master/example/src/clojure/codox/markdown.clj)

  :main process-wrapper.main)
