(defproject scheje "0.2.13"
  :description "A Scheme Interpreter on Top of Clojure"
  :url "https://turbopape.github.io/scheje"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/clojurescript "1.7.228"]]

  :clean-targets ^{:protect false} ["target"]
  
  :plugins [[lein-cljsbuild "1.1.2"]]

  :cljsbuild {
              :builds [{:id "scheje-node-repl"
                        :source-paths ["src"]

                        :compiler {:main scheje.repl
                                   :output-to "target/repl_out/scheje_repl.js"
                                   :output-dir "target/repl_out"
                                   :target :nodejs
                                   :optimizations :none
                                   :source-map true}}]}

  :main scheje.repl
  :uberjar {:aot :all})
