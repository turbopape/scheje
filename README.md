# Scheje - A Little Scheme Using Clojure Macros

Using a Set of Clojure Macros, Scheje is a scheme implementation on
top of Clojure.

This is a tiny project part of the
[TNTeam's entry to Clojure Cup 2015](https://github.com/parenode/clojure-cup-2015),
which is mainly a study for the greater
[parenode project](https://github.com/parenode).

Scheje interprets define, lambda, letrec,cond and has even primary support for
define-syntax:

```clojure
scheje.interperter> (scheme->clj (define a 
                                   (lambda(x) 
                                     (+ 1 x))))
;;=> #'scheje.interperter/a

scheje.interperter> (a 4)
;;=> 5

scheje.interperter> (scheme->clj (letrec*
                                  ((a 1)
                                   (b (lambda(x)(+ 1 x)))) 
                                  (+ a (b 2))))
;;=> 4


scheje.interperter> (scheme->clj  (cond ((< 1 2) true) 
                                        ((> 3 4) false) 
                                        (else (+ 2 3))))

;;=> true

scheje.interperter> (scheme->clj  (define-syntax add 
                                    (syntax-rules (with) 
                                                  ((add a with b) (+ a b)))))

;;=> #'scheje.interperter/add
scheje.interperter> (add 1 with 4)
;;=> 5

```
Actually, all of these constructions are passed to a "big" macro
containing a match clause that translates these expression to a more
or less direct Clojure counterpart, except for define-syntax that
internalizes the literals list (as symbols pointing to a "keywordized"
version of themselves), and generates an fn that encompasses a
generated match clause to provide for the pattern rows template.

It is possible to invoke any of these evaluations by passing strings
containing the expression to evaluate, using the eval-scheme function:

```clojure

scheje.interperter> (eval-scheme  "(define-syntax add-again 
                                    (syntax-rules (with) 
                                                  ((add a with b) (+ a b))))")

;;=> #'scheje.interperter/add-again
scheje.interperter> (add-again 1 with 3)
;;=> 4
```
So it may be pretty easy to integrate scheje with a REPL.

Now time to test scheme's hello world, the mighty append:

```clojure
scheje.interperter> (scheme->clj (define append
                                   (lambda (l1 l2)
                                      (cond
                                        ((null? l1) l2)
                                        (else (cons (car l1) (append (cdr l1) l2)))))))
;;=> #'scheje.interperter/append
scheje.interperter> (scheme->clj (append '(1 2 3) '(4 5 6)))
;;=> (1 2 3 4 5 6)

```

IT WORKS !!!
