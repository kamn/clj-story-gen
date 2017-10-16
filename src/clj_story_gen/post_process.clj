(ns clj-story-gen.post-process)

(def replace-words 
  [[#"emperor" "CEO"]
   [#"clothes" "softwares"]
   [#"dressed" "softwared"]
   [#"soldiers" "employees"]
   [#"theatre" "finance"]
   [#"suit" "suite"]
   [#"coat" "app"]
   [#"king" "CEO"]
   [#"cabinet" "board meeting"]
   [#"dressing-room" "software-room"]
   [#"city" "company"]
   [#"gay" "rich"]
   [#"strangers" "investors"]
   [#"swindlers" "contractors"]
   [#"weavers" "rockstars"]
   [#"cloth" "software"]]) 

(defn replace-str-vec [str [p v]]
  (clojure.string/replace str p v))

(defn update-story [story]
  (reduce replace-str-vec story replace-words))   

(defn get-story []
  (print (update-story (slurp "ens.txt"))))
   