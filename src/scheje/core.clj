(ns scheje.core
  (:require [scheje.interpreter :refer [eval-prog]]))


;; Samples
;; let is in the root env provided in interpreter.clj

(eval-prog  '((define-syntax let
                (syntax-rules ()
                              ((let ((var expr) ...) body ...)
                               ((lambda (var ...) body ...) expr ...))))
              (let ((x 1)(y 2)) (+ x y))))

(eval-prog '((define append
               (lambda (l1 l2)
                  (cond
                    ((null? l1) l2)
                    (else (cons (car l1) (append (cdr l1) l2))))))
             (append '(1 2 3) '(4 5 6))))
