(ns gops.algs (:require [clojure.pprint :refer [pprint]]
                        [clojure.string :refer [split lower-case]]))


(defn reverse-sequence "Given a one dimensional array of data write a function that returns a
                new array with the data reversed. Don't just use the reverse function 
                that is built into your environment."
  [sequence]
  (loop [idx (- (count sequence) 1) new-seq []]
    (if (> idx -1)
      (recur (dec idx) (conj new-seq (nth sequence idx)))
      new-seq)))

(defn group-words "Write a quick and dirty program (not just a function) to print a count
                    of all the different words in a text file. Use any reasonable definition
                    of word that makes logical sense or makes your job easy."
  [data]
  (let [words (map lower-case (split data #"\s"))]
    (sort-by val > (reduce (fn [acc val] (update acc val #(if (nil? %) 1 (inc %)))) {} words))))

(defn random-number-generator "Write a function that returns M random non-negative integers 
                               less than some value N, where M and N are arbitrary integers,
                               and each output integer is unique."
  [n m]
  (loop [sequence #{} value (rand-int n)]
    (if (= m (count sequence))
      sequence
      (recur (conj sequence value) (rand-int n)))))

(defn random-sequence-extractor "Given a one dimensional array of data write a function that
                                 return M random elements of that array. Each element must be
                                 from a different position in the array. Don't just use the
                                 sample function that is built into your environment."
  [sequence m]
  (let [sequence-count (count sequence)
        max-extractable-elements (min m sequence-count)]
    (loop [extracted-elements #{}
           index (rand-int sequence-count)]
      (if (= max-extractable-elements (count extracted-elements))
        extracted-elements
        (recur (conj extracted-elements (nth sequence index)) (rand-int sequence-count))))))

(defn substr "Given two strings, s and t , write a function that tests whether t is a substring
              contained within s. The function should return the integer index within s of the
              first occurrence of t, or -1 if no match is found. You may not use regular
              expressions in your solution (unless you also implement the regex library)."
  [str substr]
  (loop [char-idx 0 char-subidx 0 first-index -1]
    (let [chars-to-end (- (count str) char-idx 1)
          chars-to-end-sub (- (count substr) char-subidx 1)]
      (if (or (= chars-to-end 0) (= chars-to-end-sub 0))
        (if (> chars-to-end-sub chars-to-end) -1 first-index)

        (let [char-str (nth str char-idx)
              char-substr (nth substr char-subidx)
              chars-equal (= char-str char-substr)]
          (if chars-equal
            (let [new-first-index (if (= -1 first-index) char-idx first-index)]
              (recur (inc char-idx) (inc char-subidx) new-first-index))
            (recur (inc char-idx) 0 -1)))))))

(defn- node-walk [graph node-value g-set b-set]
  (let [node-edges (get graph node-value)]
    (loop [idx 0 local-g-set (conj g-set node-value)]
      (if (= idx (count node-edges))
        [false (disj local-g-set node-value) (conj b-set node-value)]
        (let [edge (nth (seq node-edges) idx)]
          (if (contains? local-g-set edge)
            [true local-g-set b-set]
            (if (contains? b-set edge)
              (recur (inc idx) local-g-set)
              (let [[cyclic next-g-set] (node-walk graph edge (conj local-g-set edge) b-set)]
                (if (= true cyclic) [true next-g-set] (recur (inc idx) next-g-set))))))))))

(defn- is-cyclic "Return a boolean indicating if the proposed graph has a cycle or not"
  ([graph] (is-cyclic graph (rand-nth (seq (keys graph))) (apply conj #{} (keys graph))  #{} #{}))
  ([graph node-value w-set g-set b-set]
   (if (empty? w-set)
     false
     (let [next-w-set (disj w-set node-value)
           [cyclic next-g-set next-b-set] (node-walk graph node-value g-set b-set)]
       (if (= true cyclic)
         true
         (is-cyclic graph (rand-nth (seq next-w-set)) next-w-set next-g-set next-b-set))))))

(defn add-node "Write a function that creates a node in a graph"
  ([graph value]
   (if (or (get graph value) (> value 60000))
     graph
     (assoc graph value #{}))))

(defn add-child-node "Write a function that creates a node in a graph"
  ([graph value parent]
   (let [new-graph-candidate (update graph parent conj value)]
     (if (and (get graph value) (get graph parent) (not (is-cyclic new-graph-candidate)))
       new-graph-candidate
       graph))))

(defn print-graph "Write a function to print out a graph"
  [graph]
  (pprint (reduce-kv (fn [acc key value] (str acc key ":" value (newline))) "" graph)))
