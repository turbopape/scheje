(ns scheje.tools
  (:require [clojure.walk :as w]))


(defn get-syntax
  [exp syntaxes]
  (first  (->> syntaxes
               (filter #(= exp (:name %))))))

(defn get-literals
  [syntaxes]
  (->> syntaxes
       (mapcat :literals)))

(defn literal?
  [exp syntaxes]
  (not (nil?  (some #{exp} (get-literals syntaxes)))))

(defn atom?
  [x]
  (not (seq? x)))

(defn pairlis
  [x y z]
  (into z  (map (fn [k v] [k v]) x y )))

(defn is-valid-symbol?
  [s]
  (not (some #{ \( \) \[ \] \{ \} \" \, \' \` \; \# \| \\  }
             (name s))))

(defn get-symbols
  [exp]
  (let [res (atom '())]
    (w/postwalk
     (fn[x]
       (when   (symbol? x)
         (swap! res conj x)))
     exp)
    @res))


(defn is-exp-valid?
 [exp]

 (every? is-valid-symbol? (get-symbols exp)))

(defn get-sexps
  [s]
  (loop [remaining s
         level 0
         result []
         current-sexp ""]
    (if (seq remaining)
      (let [cur-char (first remaining)]
        (if (not (= \newline cur-char))
          (let [cur-level (cond
                            (= cur-char \() (inc level)
                            (= cur-char \)) (dec level)
                            :else level)

                result (if (zero? cur-level)
                         (conj result (str  current-sexp cur-char))
                         result)
                
                new-current-sexp (if (zero? cur-level)
                                   ""
                                   (str current-sexp cur-char))]

            (recur (rest remaining)
                   cur-level
                   result
                   new-current-sexp))
          (recur (rest remaining)
                 level
                 result
                 current-sexp)))
      result)))


(defn load-prog-from-file
  [f]
  (->> (slurp f)
       (get-sexps)
       (map read-string)))
