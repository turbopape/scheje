# scheje - A little Scheme on Top of Clojure

<img src="./scheje-logo.jpg" size="50%"
 alt="scheje logo" title="The Inky Lambda" align="right" />
 
 >"If you give someone Fortran, he has Fortran. If you give someone Lisp, he has any language he pleases."
 - Guy L. Steele
 
Now using the magnificent eval/apply dance (as in page 13 of the
Lisp 1.5 Manual), *scheje* is a tiny scheme implementation on Top of
Clojure.

Now *scheje* properly implements *define-sytnax*, ellipsis is properly
expanded into relevant symbols, which can be respectively used in the
generated *syntax-rules* templates. In fact, even *let* is implemented in
terms of *define-syntax*.

the two defining forms, *define* and *define-syntax* generate new
environments as per the bindings they introduce. Helper functions that
interpret these along with other evaluations will be discussed later.

This implementation greatly improves the latest version, which used to
use Clojure macros. Using lists evaluation, it is possible to use
scheje as a library on top of ClojureScript as well.
## Usage

The main interpretation function is *form-eval* in the *interpreter*
name-space.


You give it a form and an environment, and you get your evaluation:
```clojure
(form-eval '(+ x y) '{x 1 y 2})
;;=> 3
```

But there is a more advanced function *eval-prog*, that evaluates a
whole program. This function is bootstrapped with a starting
environment (implementing some basic symbol definitions and the *let*
form defined with *define-syntax*). It also does what it takes to only
show the latest form evaluation in its input program.

Here are some examples:

```clojure
(eval-prog  '((define-syntax let
                (syntax-rules ()
                              ((let ((var expr) ...) body ...)
                               ((lambda (var ...) body ...) expr ...))))
              (let ((x 1)(y 2)) (+ x y))))
;;=>3
```
This example shows how we did to introduce the let form, though it is
already implemented in the root environment used by scheje, so you
don't have to define it every time you use *eval-prog*

Analogous to *let*, here's how one can declare *and* using a recursive
*define-syntax*:
```clojure
(eval-prog '((define-syntax and
               (syntax-rules ()
                             ((and x) x)
                             ((and) true)
                             ((and x y ...) (if x (and y ...) false))))
             (and true true true  true true false)))
;;=> false
```
Same here, this is already defined in *root-env* so you don't have to
define it every time you use *eval-prog*. *or* is defined in a same
way:
```clojure
(eval-prog '((define-syntax or
               (syntax-rules ()
                             ((or x) x)
                             ((or) true)
                             ((or x y ...) (if x true (or y ...)))))
             (or false false false true false)))
;;=> true
```

And, last but not least, here is the must-have *append* function, necessary for
every decent scheme implementation!
```clojure
(eval-prog '((define append
               (lambda (l1 l2)
                  (cond
                    ((null? l1) l2)
                    (else (cons (car l1) (append (cdr l1) l2))))))
             (append '(1 2 3) '(4 5 6))))
;;=> '(1 2 3 4 5 6)
```


Also, *quote*, *quasiquote*, *unquote* and *unquote-splicing* are
supported:

```clojure
(form-eval '(quasiquote
             (1
              (+ 1 (unquote-splicing (cdr '(1 2 3)))) 
              (unquote  (+ 1 a)))) 
           { 'a 5})
;;=> (1 (+ 1 2 3) 6)
```

## Lexical Scoping
To prevent symbol capture, when expanded, each expression's symbols
are stored in its own scope. This hopefully avoids name clash:

In the following example, a is defined at the root-env, but "inner" a
is retruned from the *let* macro:
```clojure
(eval-prog '((define a "outer")
             (let ((x (and false false))
                   (a "inner"))
               (if x "_" a))))
;;=> "inner"
```

If we access a symbol outside the macro, we get its root binding:

```clojure
(eval-prog '((define a "outer")
             (let ((x (and false false))
                   (a "inner"))
               (if x "_" a))
             a))
;;=> "outer"
```
This still needs testing.

## License

Copyright © 2016 Rafik Naccache
Distributed under the terms of the The MIT License.
