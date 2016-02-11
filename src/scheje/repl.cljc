(ns  scheje.repl
  (:require [scheje.interpreter :refer [eval-prog-with-env eval-exp-with-env]]
            [scheje.library :refer [root-env scheje-version]]
            [scheje.tools :as tools]
            #?(:cljs [cljs.nodejs :as nodejs])
            #?(:cljs [cljs.reader :as cljs-reader]))
  #?(:clj (:gen-class)))

(def exec-env (atom root-env))
(def current-sexp (atom ""))

#?(:cljs (do
           (nodejs/enable-util-print!)
           (def rl (js/require "readline-sync"))
           (defn js-prompt
             []
             (.question rl ""))))

(defn -main 
  "a Little REPL for Scheje"
  [& args]
  (println "Scheje" scheje-version "REPL by @turbopape. May The force be With you!!\n"
           ",q (or CTRL-C) to exit. ,l <file> to load a file. ,r to reload root environmnent")
  
  (loop [input #?(:clj  (tools/rm-comments (read-line))
                  :cljs  (js-prompt) )]
    (let [input-commands #?(:clj (clojure.string/split input #"\s")
                            :cljs (js->clj (.match input (js/RegExp. "\\S+" "g"))))
          switch (first input-commands)
          is-complete-sexp? (>  (count (tools/get-sexps input)) 0)]
      (if (not (= ",q" switch))
        (do
          (try
            (cond
              (= ",r" switch) (do  (reset! exec-env root-env) (println "Reloaded Base Environment!")) 
              (= ",l" switch) (let [scm-prog (tools/load-prog-from-file (second input-commands))
                                    file-eval (eval-prog-with-env @exec-env scm-prog)
                                    last-eval (->> file-eval  :evals (map  #(get % 1)) last)]
                                (if (nil? (:error last-eval))
                                  (do
                                    (swap! exec-env merge (:env file-eval))
                                    (println ";; Successfully loaded: " (second input-commands) "with result: " last-eval ))
                                  (println ";Error: " (:error last-eval)" in Loading file " (second input-commands) )))
              :else
              (when is-complete-sexp?
                (let [[new-env the-eval] (eval-exp-with-env @exec-env (#?(:clj clojure.tools.reader/read-string
                                                                           :cljs cljs-reader/read-string) input) )]
                  (if (nil? (:error the-eval))
                    (do
                      (swap! exec-env merge new-env)
                      (println ";=> " the-eval))
                    (println ";Error: " (:error the-eval))))))
            (catch  #?(:clj Exception :cljs js/Error) e (println ";Error: " (str e))))
          (recur (if is-complete-sexp?
                   #?(:clj  (tools/rm-comments  (read-line))
                      :cljs  (tools/rm-comments (js-prompt))  )
                   #?(:clj (str input (tools/rm-comments  (read-line)))
                      :cljs (str input (tools/rm-comments  (js-prompt)))))))
        #?(:clj (System/exit 0)
           :cljs (.exit js/process) )))))

#?(:cljs (set! *main-cli-fn* -main))
