(ns scheje.interpreter-test
  (:require [clojure.test :refer :all]
            [scheje.interpreter :refer :all]))

(deftest eval-test
  (testing "(form-eval '(+ x y) '{x 1 y 2}) shall return 3"
    (is (= 3 (form-eval '(+ x y) '{x 1 y 2})))))

(deftest define-syntax-let-test
  (testing "(eval-prog  '((define-syntax let
                (syntax-rules ()
                              ((let ((var expr) ...) body ...)
                               ((lambda (var ...) body ...) expr ...))))
              (let ((x 1)(y 2)) (+ x y)))) Shall return 3"
    (is (= 3 (eval-prog  '((define-syntax let
                (syntax-rules ()
                              ((let ((var expr) ...) body ...)
                               ((lambda (var ...) body ...) expr ...))))
                           (let ((x 1)(y 2)) (+ x y))))))))

(deftest define-syntax-and-test
  (testing "(eval-prog '((define-syntax and
               (syntax-rules ()
                             ((and x) x)
                             ((and) true)
                             ((and x y ...) (if x (and y ...) false))))
             (and true true true  true true false))) Shall return false"
    (is (= false  (eval-prog '((define-syntax and
                                 (syntax-rules ()
                                               ((and x) x)
                                               ((and) true)
                                               ((and x y ...) (if x (and y ...) false))))
                               (and true true true  true true false)))))))

(deftest define-syntax-or-test
  (testing "(eval-prog '((define-syntax or
               (syntax-rules ()
                             ((or x) x)
                             ((or) true)
                             ((or x y ...) (if x true (or y ...)))))
             (or false false false true false))) Shall return true"
    (is (= true (eval-prog '((define-syntax or
                               (syntax-rules ()
                                             ((or x) x)
                                             ((or) true)
                                             ((or x y ...) (if x true (or y ...)))))
                             (or false false false true false)))))))



(deftest append-test
  (testing "The classical append"
    (is (= '(1 2 3 4 5 6) (eval-prog '((define append
                                         (lambda (l1 l2)
                                            (cond
                                              ((null? l1) l2)
                                              (else (cons (car l1) (append (cdr l1) l2))))))
                                       (append '(1 2 3) '(4 5 6))))))))


(deftest quote-test
  (testing "Quoting, unquoting, unquote-splicing..."
    (is (= '(1 (+ 1 2 3) 6) (form-eval '(quasiquote
                                         (1
                                          (+ 1 (unquote-splicing (cdr '(1 2 3)))) 
                                          (unquote  (+ 1 a)))) 
                                       { 'a 5})))))
(deftest scope-test-1
  (testing "Scoping test 1: "
    (is (= "inner" (eval-prog '((define a "outer")
                                (let ((x (and false false))
                                      (a "inner"))
                                  (if x "_" a))))))))

(deftest scope-test-2
  (testing "Scoping test 2: "
    (is (= "outer" (eval-prog '((define a "outer")
                                (let ((x (and false false))
                                      (a "inner"))
                                  (if x "_" a))
                                a))))))



(deftest macros-hygiene-1
  (testing "Symbols clash 1 in pages 2,3 of http://www.cs.indiana.edu/~dyb/pubs/bc-syntax-case.pdf "
    (let [clash-1 (eval-prog '(
                               (define-syntax or2
                                 (syntax-rules ()
                                               ((or2 e1 e2) (let ((t e1)) (if t t e2) ))))
                               (let ((t true)) (or2 false t ))
                               ))]
      (is (= true clash-1)))))

(deftest macros-hygiene-2
  (testing "Symbols clash 2 in pages 2,3 of http://www.cs.indiana.edu/~dyb/pubs/bc-syntax-case.pdf ")
  (let [clash-2 (eval-prog '(
                             (let ((if (lambda(x y z) "oops")))
                               (let ((g false))
                                 (if g g false)))
                             ))]
    (is (= false clash-2))))


