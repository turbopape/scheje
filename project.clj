(defproject scheje "0.2.5"
  :description "A Scheme Interpreter on Top of Clojure"
  :url "https://turbopape.github.io/scheje"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]]
  :main scheje.repl
  :uberjar {:aot :all})
