(ns clj-story-gen.core
  (:gen-class)
  (:require [clj-story-gen.post-process :as pp]
            [clj-story-gen.markov :as m]
            [clj-story-gen.sim :as sim]
            [clj-story-gen.template :as t]
            [selmer.parser :as selmer]))


(defn -main
  ""
  [& args]
  (sim/get-story))
