# scheje - A little Scheme on Top of Clojure
[![License MIT](https://img.shields.io/badge/License-MIT-blue.svg)](http://opensource.org/licenses/MIT)
[![Build Status](https://travis-ci.org/turbopape/scheje.svg?branch=master)](https://travis-ci.org/turbopape/scheje)
[![Clojars Project](https://img.shields.io/clojars/v/scheje.svg)](https://clojars.org/scheje)
[![Gratipay Team](https://img.shields.io/gratipay/team/scheje.svg)](https://gratipay.com/scheje/)
[![Slack Team](https://img.shields.io/badge/chat-%23%20team-yellowgreen.svg)](https://scheje.slack.com/shared_invite/MzA5NjE3Njg3MDgtMTQ1OTQzNzMzOS1hMGY3YjI3Mjlk)
 
 >"If you give someone Fortran, he has Fortran. If you give someone Lisp, he has any language he pleases."
 - Guy L. Steele
 
Now using the magnificent eval/apply dance (as in page 13 of the
Lisp 1.5 Manual), *scheje* is a tiny scheme implementation on Top of
Clojure.


*scheje* properly implements *define-syntax*, ellipsis is properly
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
You can play with scheje on-line with the [Web REPL](http://turbopape.github.io/scheje/).

The main interpretation function is *form-eval* in the *interpreter*
name-space.

You give it a form and an environment, and you get your evaluation:
```clojure
(form-eval '(+ x y) '{x 1 y 2})
;;=> 3
```

The function *eval-prog*, evaluates a whole program. This function is 
bootstrapped with a starting
environment, called the library (see [library.cljc](https://github.com/turbopape/scheje/blob/master/src/scheje/library.cljc) for the contents of the library). When launched, eval-prog evaluates all the forms, only showing the value of the last one.

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
Also, named let, let*, and many more are available. Please refer to
the test files for the interpreter namespace to see possible uses.

## Lexical Scoping in *let* Macros

To prevent symbol capture when using the *let* macro, when expanded,
each *let* introduced expression's symbols are stored in its own
scope. This avoids name clash:

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
## Hygienic Macros 

Inspired by
[KFFD](http://web.cs.ucdavis.edu/~devanbu/teaching/260/kohlbecker.pdf)
Algorithm. Now Scheje appends a timestamp with respect to the
iteration in which every form is being expanded, thus preventing from
inadvertently capturing symbols across different expansion stages.
If some symbols having timestamps could not be evaluated, Scheje tries to
evaluate their "root" form, i.e, looks if their name stripped out of the timestamps isn't
bound in the execution environment. This 'sort of' forces capture of such symbols,
as to see if they were intended to be passed as globals, for instance.

For instance, these two exmaples work properly:
```clojure
(eval-prog '(
             (define-syntax or2
               (syntax-rules ()
                             ((or2 e1 e2) (let ((t e1)) (if t t e2) ))))
             (let ((t true)) (or2 false t ))
             ))
;;=> true
```
and 

```clojure 
(eval-prog '( 
(let ((if (lambda(x y z) "oops")))
 (let ((g
false)) (if g g false))) ))
 ;;=> false 
``` 

### ClojureScript Support
scheje is written using reader conditionals. the interpreter namespace
can be used in a clojurescript project right away, in the browser, 
or in node.js if you want to use the file loading facilities.

### A little REPL 
Now a **REPL** namespace is shipped, exposing a little REPL permitting
to evaluating expressions and loading files. 

If you want to run it in Clojure/JVM, just clone the repo and launch
```shell
lein run
```
You can also build a ClojureScript/Node.js REPL and enjoy the ultra fast startup compared to JVM's version. Simply
proceed like so:
```shell
#install npm dependencies
npm install readline-sync
# then build the node app
lein cljsbuild once
# and enjoy scheje.js :)
node target/repl_out/scheje_repl.js
```
## Features
Please refer to the [Changelog](https://github.com/turbopape/scheje/blob/master/CHANGELOG.md) and the [interpreter_test.clj test file](https://github.com/turbopape/scheje/blob/master/test/scheje/interpreter_test.clj) to get a comprehensive list of the features and usage of *scheje*.

## TODOS - Wanna Contribute?
You can see how you can help by seeing the [open issues](https://github.com/turbopape/scheje/issues)

## Credits
scheje's Node.js REPL uses [readLineSync](https://github.com/anseki/readline-sync) from @anseki, which is licensed under the MIT License. 
## License
Copyright © 2016 Rafik Naccache
Distributed under the terms of the The MIT License.
