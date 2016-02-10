(ns scheje.unifier
  (:require #?(:clj  [clojure.core.match :refer [match]]
               :cljs [cljs.core.match :refer-macros [match]])
            [scheje.tools :refer [literal?]]
            [clojure.zip :as z]
            [clojure.walk :as w]))

(defn occurs?
  [s t]
  (if (seq? t)  ;; If second term is a seq
    (let [zpr  (z/zipper seq? identity conj t)]
      ;; We create a zipper to walk it
      (loop [loc (-> zpr z/down)]
        ;; We begin at the first element of the zipper
        (if (-> loc z/end?)
          false
          ;; We reached the end, yet we were not able
          ;; to find the first term. It does not occur
          (if (= s (-> loc z/node))
            true
            ;; One node of our zipper happens to be equal
            ;; to the first term. We immediately return true!
            (recur (-> loc z/next))))))
    ;; else we recur over the next zipper element
    (= s t))) ;; If t is not a seq, we say that s occurs in t
;; if they are equal

;; Subst is a map between variables and their replacement : {:a '(:d :c) :b :f} ...
;; this makes this impl not thread safe. However, I had to use this because I am
;; Using trampolining and CPS, so I can't pass return values(I pass functions btw calls) and then
;; had to use global state.
;; I also append the iteration number to variables referred to in ellipsis so I don't
;; Have a clash in case of ellipsis nesting.


(def s (atom {}))

(def iteration-id (atom 0))

(def scope (atom 0))

(defn apply-subst
  [u]
  (if (seq? u); If u is a seq
    (replace @s u)
    ;; We apply the value referred by the s atom
    ;; as a substitution map so to respectively
    ;; replace u terms by their counterparts
    ;; in s
    (if-let [subst  (get @s u)] ;; Else, u is a scalar
      subst ;; If u occurs in s we return its counterpart
      u))) ;; else we leave it as is.

(declare uni) ;; uni declaration

(defn dynamic-free-symbols
  [exp ts env]
  (w/postwalk
   (fn[x]
     (if (symbol? x)
       (cond
         (or
          (some #{x} (->> (get env :keywords)))
          (some #{x} (->> (get env :syntax)
                          (map :name )))
          (some #{\#} (name  x))) x
         :else (symbol  (str  (name x) \# ts)))
       x))
   exp))

(defn try-subst
  [ts env u v ks kf]
  (let [u (apply-subst u)] ;; First, we exchange u for any known
    ;; substitute, so we don't unify the same
    ;; thing with two different terms

    (if (not (symbol? u)) ;;This is not a symbol, it is an expression
      #(uni ts env u v ks kf)    ;; Launch uni, the "main" expressions unifier
      (let [v (apply-subst v )] ;; Same logic for right hand expression
        ;; term
        (cond
          ;; It'll be processed by its function.
          (or (= '_ u)
              (= u v)) #(ks @s) ;; The terms are equal, nothing to do, we
          ;; run the success function over the same content of s

          (literal? u @s) #(kf {:error (str "literal: " u " mismatch - with: " v )})
          ;; u is different than v, and it is a literal, so this is a match error

          (occurs? u v) #(kf {:error  (str  "Cycle occuring between " u " and " v)}) ;; Occur Check error

          :default #(ks  (swap! s
                                assoc
                                u
                                (dynamic-free-symbols v ts env ))))))))
;; Else, We found a new substitution
;; to be added to our substitutions atom s

(defn get-symbol-idx
  [s]
  (let [parsed #?(:clj (clojure.string/split s #"#")
                  :cljs (js->clj (.match s (js/RegExp. "#"))))]
    (if (> (count parsed) 1)
      {:sym (first parsed)
       :idx #?(:clj (. Integer parseInt (last parsed))
               :cljs (js/parseInt (last parsed)))}
      {:sym (first parsed) :idx 0})))

(defn inc-symbols
  [p]
  (w/postwalk
   (fn[x]
     (if (and  (not (= x '...))
               (symbol? x))
       (let [sb-idx (get-symbol-idx (name  x))]
         (symbol  (str (:sym sb-idx) \# @iteration-id )))
       x))
   p))



(defn uni
  [ts env u v ks kf]
  (swap! iteration-id inc)
  ;; First of all, I inc the global iteration-id atom; so
  ;; I can get proper symbol name for nested ellipsis variables
  (cond
    (symbol? u) #(try-subst ts env u v  ks kf) ;; Try to find substitutions if the left hand
    ;; term is a symbol 
    

    ;; If we have two symbolic expressions
    ;; with no name clash, we unify all of
    ;; their inner terms, one by one, using
    ;; a recursion.We'll use this inner function
    ;; for this.
    (and
     (seq? u)
     (seq? v))  (match [u]
                           [([exp '... & r] :seq)] (letfn [(parse-ellipsis
                                                             [u v r]
                                                             (if (nil? v)
                                                               (fn[& _] (ks @s))
                                                                 (fn[& _]
                                                                   (let [n-exp (inc-symbols u)]
                                                                     (uni ts
                                                                          env
                                                                          n-exp
                                                                          (first v)
                                                                          (fn[_]
                                                                            (parse-ellipsis
                                                                             n-exp
                                                                             (next v)
                                                                             r))
                                                                          (if (seq r) (fn[& _]
                                                                                        (uni ts
                                                                                             env
                                                                                             r
                                                                                             v
                                                                                             ks
                                                                                             kf))
                                                                              (fn [& _] (ks @s))))))))]
                                                     (trampoline parse-ellipsis
                                                                 exp
                                                                 v
                                                                 r))

                           :else (cond
                                   (or  (= (count u) (count v))
                                        (some #{'...} u)) (letfn [(internal-symbols  
                                                                     [u v]
                                                                     (if (nil? u)
                                                                       #(ks @s)
                                                                       ;; End of the recursion, run success on the value
                                                                       ;; referred by s
                                                                       (fn[] ;; else, recurse over uni
                                                                         (cond
                                                                           (= '... (second u)) (uni
                                                                                                ts
                                                                                                env
                                                                                                u
                                                                                                v
                                                                                                (fn[_]
                                                                                                  (internal-symbols (next u)
                                                                                                                    (next v)))
                                                                                                kf)
                                                                           (= '... (first u) ) #(ks @s)
                                                                           :else (uni ts
                                                                                      env
                                                                                      (first u)    
                                                                                      (first v)                                        
                                                                                      (fn[_]
                                                                                        (internal-symbols (next u)
                                                                                                          (next v)))
                                                                                      kf)))))]
                                                            (trampoline internal-symbols  
                                                                        u
                                                                        v))
                                   :else #(kf {:error (str "different length in "u "and " v)})))
    ;; else we had a symbol clash!
    :else (if (= u v)
            #(ks @s)
            #(kf {:error (str "Clash in " u " and " v)}))))

(defn unify
  [u v ts env]
  (let [_  (reset! s {})
        _ (reset! iteration-id 0)]
    ;; I reset the global atoms so
    ;; to have a clean unification for patterns
    (trampoline uni
                ts
                env
                u
                v
                identity
                identity)))
