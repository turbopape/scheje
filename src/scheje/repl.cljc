(ns scheje.repl
  (:require [scheje.interpreter :refer :all]
            [scheje.library :refer :all]
            [scheje.tools :refer :all])
  (:gen-class))

(def exec-env (atom root-env))
(def current-sexp (atom ""))


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
              (= ",l" switch) (let [scm-prog (load-prog-from-file (second input-commands))
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
                      (reset! exec-env new-env)
                      (println ";=> " the-eval))
                    (println ";Error: " (:error the-eval))))))
            (catch  Exception e (println ";Error: " (str e))))
          (recur (if is-complete-sexp?
                   (read-line)
                   (str input (read-line)))))
        (System/exit 0)))))
