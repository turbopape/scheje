(ns scheje.interpreter
  (:require #?(:clj  [clojure.core.match :refer [match]]
               :cljs [cljs.core.match :refer-macros [match]])
            [scheje.tools :as tools] 
            [scheje.unifier :as unifier]
            [scheje.expander :as expander]
            [scheje.library :refer [root-env]]))

(def ts (atom 0))

(declare form-eval)

(defn form-apply
  [exp a]
  (match [exp]
         ;; The Core Functions 
         [([(f :guard tools/atom?) & r] :seq )] (cond
                                                  (= f 'length) (count (first  r))
                                                  (= f 'eq?)(apply = (into [] r))
                                                  (= f 'pair?) (seq? (first r))
                                                  (= f '=) (apply = (into [] r))
                                                  (= f 'null?) (=  '(()) r)
                                                  (= f 'zero?) (zero? (first r))
                                                  (= f 'car) (-> r first first)
                                                  (= f 'cdr) (-> r first rest )
                                                  (= f 'cons) (cons (first r) (second r)) 
                                                  (= f 'atom?) (tools/atom? (first r))
                                                  (= f 'not) (not (first r))
                                                  (= f 'symbol?) (symbol? (first r))
                                                  (= f 'number?) (number? (first r))
                                                  (= f 'vector?) (vector? (first r))
                                                  (= f 'vector) (into [] r)
                                                  (= f 'vector-ref) (apply get r)
                                                  (= f 'display) (apply println (into [] r))
                                                  (= f '+) (apply + (into [] r))
                                                  (= f '-) (apply - (into [] r))
                                                  (= f '_slash_) (apply / (into [] r))
                                                  (= f '/) (apply / (into [] r))
                                                  (= f '*) (apply * (into [] r))
                                                  (= f '<) (apply < (into [] r))
                                                  (= f '>) (apply > (into [] r))
                                                  (= f '<=) (apply <= (into [] r))
                                                  (= f '>=) (apply >= (into [] r))
                                                  :else (let [f-eval (form-eval f a)]
                                                           (if (= 'lambda (first f-eval))
                                                             (form-apply (cons f-eval r) a)
                                                             (throw (#?(:clj Exception.
                                                                        :cljs js/Error.) 
                                                                     (pr-str "Can't apply " f-eval " on " r))))))

         [([(['lambda parms body]:seq) & args ]:seq)](cond
                                                  (symbol? parms) (form-eval body
                                                                             (assoc a  parms args))
                                                  
                                                  (some #{'.}  (map (comp
                                                                     symbol
                                                                     :sym
                                                                     unifier/get-symbol-idx name) parms)) (let
                                                                                                              [nparms (-> parms butlast butlast)
                                                                                                               rparm (last parms)]
                                                                                                            (form-eval body (->
                                                                                                                             (tools/pairlis
                                                                                                                              nparms
                                                                                                                              (take (count nparms) args) a)
                                                                                                                             (assoc rparm (drop (count nparms) args)))))
                                                  :else (form-eval body (tools/pairlis parms args a)))
         :else (form-eval (cons (form-eval (first exp) a) (rest exp)) a)))

(defn evcon
  [conds a]
  (cond
    (form-eval (-> conds first first) a) (cond (and  (tools/atom? (-> conds first rest first)) ;; (a-cond => second-cond)
                                                     (= '=> (-> conds first rest first))) (-> conds first rest rest first) 
                                           :else (form-eval (-> conds first rest first) a))
    :else (evcon (rest conds) a)))

(defn evlis
  [exps a]
  (map #(form-eval % a) exps))

(defn define-syntax
  [a
   syn-name
   literals
   pattern-rules]
  (update-in a [:syntax ] conj {:name syn-name
                                :literals literals
                                :rules  pattern-rules}))
(defn define
  [a
   sym
   binding]
  (assoc a sym
         (form-eval binding a)))

(defn sym-set!
  [a
   sym
   binding]
  (let [exists? (get a sym)]
    (if (not (nil? exists?))
      (define a sym binding)
      (throw (#?(:clj Exception. :cljs js/Error.)
              (str "set! can't find symbol: " sym))))))

(defn form-eval-quasi
  [exp a]
  (cond
    (tools/atom? exp) exp 

    (or  (= '() exp) 
         (nil? exp)) '()

    (or (number? exp)
        (string? exp)
        #?(:clj (rational? exp))) exp
    
    (tools/atom? (first exp)) (cons (first exp) (form-eval-quasi (rest exp) a))
    (= (-> exp first first) 'unquote)  (cons (form-eval (-> exp first second ) a)
                                             (form-eval-quasi (rest exp) a))
    (= (-> exp first first) 'unquote-splicing) (into (form-eval (-> exp first second ) a)
                                                     (form-eval-quasi (rest exp) a))
    :else (cons (form-eval-quasi (-> exp first) a)
                (form-eval-quasi (rest exp ) a))))

(defn form-eval
  [exp a]
  (cond
    
    (or  (= '() exp) 
         (nil? exp)) '()

    (or (number? exp)
        (string? exp)
        #?(:clj (rational? exp))) exp
    
    (tools/atom? exp) (let [scope (get-in a [:scopes exp])]
                        (if (not (nil? scope))
                          (get scope exp)
                          (let [from-root (get a exp)]
                            (if (not (nil? from-root))
                              from-root
                              (if (some #{\#} (name  exp) )
                                (form-eval  (symbol  (:sym  (unifier/get-symbol-idx (name  exp)))) a) 
                                ;; maybe a macro symbol that we want captured, a function name?
                                (throw ( #?(:clj Exception. :cljs js/Error. ) (pr-str  "No binding found for " exp))))))))

    (tools/atom? (first exp)) (let [some-syn (tools/get-syntax (first exp) (:syntax a))]
                                (cond
                                  (not (nil?  some-syn)) (let [{:keys [rules]} some-syn]  
                                                           (loop [remaining rules]
                                                             (let [cur-rule (first remaining)
                                                                   cur-pattern (first cur-rule)
                                                                   cur-tpl (second cur-rule)
                                                                   a-match (unifier/unify cur-pattern exp (swap! ts inc) a)]
                                                               (cond
                                                                 (nil? (get a-match :error)) (let [expanded-tpl
                                                                                                   (expander/expand-w-bindings cur-tpl
                                                                                                                               a-match)]
                                                                                               (form-eval expanded-tpl
                                                                                                          (assoc-in  a
                                                                                                                     [:scopes expanded-tpl]                                                                                                                  (conj root-env  a-match)))) 
                                                                 :else (if (seq remaining)
                                                                         (recur (rest remaining))
                                                                         (throw ( #?(:clj Exception. :cljs js/Error. ) (pr-str "Error in resolving syntax in: "
                                                                                                                            exp))))))))
                                  

                                  
                                  (or #?(:clj (rational? (first exp)))
                                      (string? (first exp))
                                      (number? (first exp))) (throw ( #?(:clj Exception. :cljs js/Error. ) (pr-str "error: The Scalar: `" (first exp)
                                                                                                                "` Cannot be Applied on " (rest exp) "!!")))
                                  (= (first exp) 'lambda) exp
                                  (= (first exp) 'quasiquote) (form-eval-quasi (second exp) a )
                                  (= (first exp) 'unquote) (throw ( #?(:clj Exception. :cljs js/Error. )
                                                                   (str "error: unquote can only be called "
                                                                        "in a quasiquoted form!")))
                                  (= (first exp) 'unquote-splicing) (throw ( #?(:clj Exception. :cljs js/Error. )
                                                                            (str "error: unquote-splicing can only be"
                                                                                 "called in a quasiquoted form!")))
                                  (= (first exp) 'quote) (-> exp rest first)
                                  (= (first exp) 'cond) (evcon (rest exp) a)
                                  (= (first exp) 'if) (if (form-eval (-> exp rest first) a)
                                                        (form-eval (-> exp rest second) a)
                                                        (form-eval (-> exp rest (nth 2)) a))
                                  :else (form-apply (cons (first exp) (evlis (rest exp) a)) a)))
    :else (form-apply (cons (first exp) (evlis (rest exp) a)) a)))

(defn eval-exp-with-env
  [env exp]
  (if (tools/is-exp-valid? exp) (try
                                  (cond
                                    (and (seq? exp)
                                         (= (first exp) 'define-syntax)) [(define-syntax env
                                                                            (second exp)
                                                                            (-> exp rest second second)
                                                                            (-> exp rest second rest rest))
                                                                          (second exp)]

                                    (and (seq? exp)
                                         (= (first exp) 'set!)) [(sym-set! env
                                                                           (second exp)
                                                                           (-> exp rest rest first))
                                                                 (second exp)]
                                    
                                    
                                    (and (seq? exp)
                                         (= (first exp) 'define))(cond
                                                                   (seq? (second exp)) [(define env
                                                                                          (-> exp second first)
                                                                                          (list 'lambda
                                                                                                (-> exp second rest)
                                                                                                ( -> exp rest rest first)))
                                                                                        (second exp)]

                                                                   :else [(define env
                                                                            (second exp)
                                                                            (-> exp rest rest first))
                                                                          (second exp)])
                                    
                                    :else [env (form-eval exp env)])
                                  (catch #?(:clj Exception :cljs js/Error) e [env {:error (str "in " exp " : " e)}]))
      [env {:error (pr-str "Invalid Symbols in exp: " exp)}]))

(defn eval-prog-with-env
  [a exprs]  
  (loop [remaining exprs
         eval-result []
         env a]
    (if (seq remaining)
      (let [exp (first remaining)
            [new-env the-eval]  (eval-exp-with-env env exp)]
        (if (nil? (:error the-eval))
          (recur (rest remaining)
                 (conj eval-result [exp the-eval])
                 new-env)
          {:evals [[exp the-eval]]}))
      {:env env
       :evals eval-result})))

(def eval-prog (comp last (partial map #(get % 1)) :evals (partial eval-prog-with-env root-env)))

