(ns scheje.interpreter-test
  (:require [clojure.test :refer :all]
            [scheje.interpreter :refer :all]))

(deftest symbol-clash-1
  (testing "Symbols clash 1 in pages 2,3 of http://www.cs.indiana.edu/~dyb/pubs/bc-syntax-case.pdf "
    (let [clash-1 (eval-prog '(
                               (define-syntax or2
                                 (syntax-rules ()
                                               ((or2 e1 e2) (let ((t e1)) (if t t e2) ))))
                               (let ((t true)) (or2 false t ))
                               ))]
      (is (= true clash-1)))))

(deftest symbol-clash-2
  (testing "Symbols clash 2 in pages 2,3 of http://www.cs.indiana.edu/~dyb/pubs/bc-syntax-case.pdf ")
  (let [clash-2 (eval-prog '(
                             (let ((if (lambda(x y z) "oops")))
                               (let ((g false))
                                 (if g g false)))
                             ))]
    (is (= false clash-2))))
