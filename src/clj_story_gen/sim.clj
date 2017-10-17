(ns clj-story-gen.sim
  (:require [selmer.parser :as selmer]))
;;Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
;;Any live cell with two or three live neighbours lives on to the next generation.
;;Any live cell with more than three live neighbours dies, as if by overpopulation.
;;Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction

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

(def end-template "{{name}} died in Alaska because she didn't have any neighbors.")

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


(defn neighbours [[x y]]
  (for [dx [-1 0 1] dy (if (zero? dx) [-1 1] [-1 0 1])] 
    [(+ dx x) (+ dy y)]))

(defn neighbours-num [loc cells]
  (get (frequencies (mapcat neighbours cells)) loc 0))

(defn board-step [cells]
  (set (for [[loc n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3) (and (= n 2) (cells loc)))]
         loc)))

(defn world-step [world]
  (let [new-board (board-step (:board world))
        nei-num (neighbours-num key-cell new-board)
        neis (take nei-num (repeatedly #(rand-nth names)))]
    (-> world
      (assoc :year (inc (:year world)))
      (assoc :board new-board)
      (assoc :neighbors nei-num)
      (assoc :neighbors-list neis)
      (assoc :neighbor-names (clojure.string/join ", " neis)))))

(defn expand-nei-stories [world neighbor-name]
  (let [sub-world {:name (:name world)
                   :supplies (rand-nth supplies)
                   :neighbor neighbor-name}]
    (selmer/render neighbor-story sub-world)))

(defn explain-world [world]
  (clojure.string/join "\n"
    [(selmer/render year-template world)
     (clojure.string/join "\n" (map #(expand-nei-stories world %) (:neighbors-list world)))]))

(def history (iterate world-step world))


(defn get-story []
  (let [intro (selmer/render into-template world)
        body (clojure.string/join "\n\n" 
                (map explain-world
                  (take 3 history)))
        ending (selmer/render end-template world)]
    (print (clojure.string/join "\n\n" [intro body ending ""]))))