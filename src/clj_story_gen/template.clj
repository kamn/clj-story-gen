(ns clj-story-gen.template)

;; Names of characters (Good old Alice and Bob)
(def names 
  ["Alice" 
   "Bob" 
   "Carol" 
   "Chuck" 
   "Eve" 
   "Sybil"])

(def places 
  ["London" 
   "New York" 
   "Hong Kong" 
   "Johannesburg"])

(def disaster
  ["fire"
   "earthquake"
   "tornado"
   "flood"
   "Fire Nation attack"])

(def story-templates 
  ["%s wanted to visit %s. %s also wanted to go. There was a %s and neither could go."
   "%s lived in %s. %s came over for dinner. There was a %s and they both were hurt."
   "%s was passing though %s. %s wanted to meet up for lunch but a %s prevented it."]) 

(defn get-story []
  (let [rand-name (rand-nth names)
        rand-place (rand-nth places)
        rand-friend (rand-nth names)
        rand-disaster (rand-nth disaster)
        rand-template (rand-nth story-templates)]
     (println (format rand-template rand-name rand-place rand-friend rand-disaster))))