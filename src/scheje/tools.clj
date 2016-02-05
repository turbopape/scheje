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
