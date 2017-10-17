(ns clj-story-gen.sim
  (:require [selmer.parser :as selmer]))

(def key-cell [3 2])

(def key-name "Alice")

(def loc-types
  ["valley"
   "hill"
   "plain"
   "mountain"])

(def names 
  ["Bob" 
   "Carol" 
   "Chuck" 
   "Eve" 
   "Sybil"])

(def supplies
  ["food"
   "wood"
   "medical supplies"
   "shelter"])

(def into-template "{{name}} moved to Alaska. {{name}} settled down on a {{place}} in the remote Yukon. This is {{name}}'s story.")

(def year-template "In year {{year}}, {{name}} had {{neighbors}} neighbors: {{neighbor-names}}.")

(def neighbor-story "{{neighbor}} gave {{name}} some {{supplies}}.")

(def end-template "{{name}} died the next year in Alaska because she didn't have enough neighbors to help.")

(def board #{[2 1] [3 2] [2 3]})

;; Initial state of the world
(def world 
  {:name key-name
   :place (rand-nth loc-types)
   :neighbors 2
   :year 1
   :neighbors-list ["Bob" "Carol"]
   :neighbor-names "Bob and Carol"
   :board board} )


(defn join [delim xs]
  (clojure.string/join delim xs))

(defn nl-join [xs]
  (join "\n" xs))

;; A very simple game of life implementations
;; http://clj-me.cgrand.net/2011/08/19/conways-game-of-life/
(defn neighbors [[x y]]
  (for [dx [-1 0 1] dy (if (zero? dx) [-1 1] [-1 0 1])] 
    [(+ dx x) (+ dy y)]))

(defn neighbors-num [loc cells]
  (get (frequencies (mapcat neighbors cells)) loc 0))

(defn board-step [cells]
  (set (for [[loc n] (frequencies (mapcat neighbors cells))
             :when (or (= n 3) (and (= n 2) (cells loc)))]
         loc)))

(defn world-step [world]
  (let [new-board (board-step (:board world))
        neighbors-num (neighbors-num key-cell new-board)
        neighbors-names (take neighbors-num (repeatedly #(rand-nth names)))]
    (-> world
      (assoc :year (inc (:year world)))
      (assoc :board new-board)
      (assoc :neighbors neighbors-num)
      (assoc :neighbors-list neighbors-names)
      (assoc :neighbor-names (join ", " neighbors-names)))))

(defn expand-neighbors-stories [world neighbor-name]
  (let [sub-world {:name (:name world)
                   :supplies (rand-nth supplies)
                   :neighbor neighbor-name}]
    (selmer/render neighbor-story sub-world)))

(defn explain-world [world]
  (nl-join
    [(selmer/render year-template world)
     (nl-join (map #(expand-neighbors-stories world %) (:neighbors-list world)))]))

;; Create a lazy sequence of the entire history of the world
(def history (iterate world-step world))

(defn key-alive? [world]
  (and (< (:year world) 100) (contains? (:board world) key-cell)))

(defn get-story []
  (let [intro (selmer/render into-template world)
        body (join "\n\n" 
                (map explain-world
                  (take-while key-alive? history)))
        ending (selmer/render end-template world)]
    (print (join "\n\n" [intro body ending ""]))))