(ns scheje.expander
  (:require [scheje.unifier :as unifier]
            [scheje.tools :refer [atom?]]
            [clojure.walk :as w]))

(defn get-ordered-symbols-for
  "returns all symbols beginning with s, ordered "
  [s a-match]
  (->> a-match
       (filter (comp  (partial = (str  s)) :sym unifier/get-symbol-idx  str key))
       (map #(get % 0))
       (sort-by (comp  :idx unifier/get-symbol-idx str) >)))
  
(defn get-exp-symbols
  [exp a-match-symbols]
  (let [res (atom '())]
    (w/postwalk (fn[x]
                  (when (and  (symbol? x)
                              (contains? a-match-symbols x))
                    (swap! res conj x)))
                exp)
    @res))

(defn generate-exp
  [exp some-matches]
  ;;replaces x , y, ... in relevant parts in bindings of '(X12, Y12,...) in env
  (w/postwalk
   (fn [x]
     (if (symbol? x)
       (if-let  [the-sym (first  (filter #(= (str x)
                                             (:sym (unifier/get-symbol-idx (str %))))
                                         some-matches))]
         the-sym
         x)
       x))
   exp))

(defn repeat-exp
  [exp a-match]
  (let [syms (get-exp-symbols exp (set  (map
                                         (comp  symbol :sym unifier/get-symbol-idx str key )
                                         a-match)))
        exp-matches (->> syms
                         (mapv #(get-ordered-symbols-for % a-match)))
        can-expand? (->> exp-matches
                         (mapv count)
                         (apply = ))]
    (if can-expand?
      (loop [remaining exp-matches
             res '()]
        (if (every? nil?  (map seq remaining))
          res
          (recur (map rest remaining)
                 (conj res (generate-exp exp
                                         (map first remaining))))))
      '(:error in symbol eval for some ellipsis))))

(defn expand-exp
  [exp a-match]
  (cond 
    (atom? exp) exp
    (not (seq exp)) '()
    (seq? exp) (if (= '... (second exp))
                 (into (expand-exp (-> exp rest rest) a-match)
                       (reverse  (repeat-exp  (first exp) a-match)))
                 (cons (expand-exp (first exp) a-match)
                       (expand-exp (rest exp) a-match)))))

(defn expand-w-bindings
  [exp a-match]
  (let [expanded (expand-exp exp a-match)]
    (w/postwalk
     (fn [x]

       (let [bdng (get a-match x)]
         (if (not (nil? bdng))
           bdng
           x)))
     expanded)))
