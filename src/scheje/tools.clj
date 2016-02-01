(ns scheje.tools)


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
