(ns scheje.repl
  (:require [scheje.interpreter :refer :all]
            [scheje.library :refer :all])
  (:gen-class))

(def exec-env (atom root-env))
(def current-sexp (atom ""))
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



(defn -main 
  "a Little REPL for Scheje"
  [& args]
  (println "Scheje 0.2.5 REPL by @turbopape. May The force be With you!!\n"
           ",q (or CTRL-C) to exit. ,l <file> to load a file. ,r to reload root environmnent")

  (loop [input (read-line)]
    (let [input-commands (clojure.string/split input #"\s")
          switch (first input-commands)
          is-complete-sexp? (>  (count (get-sexps input)) 0)]
      (if (not (= ",q" switch))
        (do
          (try
            (cond
              (= ",r" switch) (do  (reset! exec-env root-env) (println "Reloaded Base Environment!")) 
              (= ",l" switch) (let [scm-prog (map read-string (get-sexps (slurp  (second input-commands))))
                                    file-eval (eval-prog-with-env! @exec-env scm-prog)
                                    last-eval (-> file-eval  :evals vals last)]
                                (if (nil? (:error last-eval))
                                  (do
                                    (reset! exec-env (:env file-eval))
                                    (println ";; Successfully loaded: " (second input-commands) "with result: " last-eval ))
                                  (println ";Error: " (:error last-eval)" in Loading file " (second input-commands) )))
              :else
              (when is-complete-sexp?
                (let [[new-env the-eval] (eval-exp-with-env! @exec-env (clojure.tools.reader/read-string input) )]
                  (if (nil? (:error the-eval))
                    (do
                      (reset! exec-env new-env)()
                      (println ";=> " the-eval))
                    (println ";Error: " (:error the-eval))))))
            (catch  Exception e (println ";Error: " (str e))))
          (recur (if is-complete-sexp?
                   (read-line)
                   (str input (read-line)))))
        (System/exit 0)))))
