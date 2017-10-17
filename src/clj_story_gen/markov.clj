(ns clj-story-gen.markov
  (:require [markov.core :as markov]))

(defn get-story []
  (print 
    (str 
      (clojure.string/join " "
        (take 100
          (markov/generate-walk (markov/build-from-file "tarzan.txt")))) "\n\n\n\n")))