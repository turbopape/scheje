# Change Log
##[0.2.15] - [2016-03-18]
###Added
- vector-ref to get the element of a vector at index.
##[0.2.14] - [2016-03-18]
###Added
- Now you can use scheme reader literals, when red from strings, using the
  *sanitize-scm->clj* and *print-clj->scm* from the *tools* namespace.
- Added vectors, with scheme reader literals #(...) using the above
  functions from the above namespace. under the hood, they are just
  Clojure vectors.
###Fixed
- Fixed applying s-exp that evaluate to lambdas.

##[0.2.13] - [2016-02-24]
###Added
- Added assoc (assoc 'a '((a 1)(b 2)...)) => '(a 1)
- Added alist-cons
- Added length
- Added cadr, cddr, etc... functions

###Fixed
- No such things as improper lists (for now). (cons atom atom) => error

##[0.2.12] - [2016-02-17]
###Added
- Added variadic lambda in name and dotted form.
- Added define (proc args)... procedure declaration form.
###Fixed
- Now you can cons into non seq elements, though there is still no
  proper/improper list distinction in scheje.

##[0.2.11] - [2016-02-10]
###Fixed
- Fixed Scheje REPLs comments processing.

##[0.2.10] - [2016-02-10]
###Fixed
- Fixed Scheje version shown in the REPLs' invite.

##[0.2.9] - [2016-02-10]
###Fixed
- Fixed eval-prog to properly work with the changes introduced in 0.2.7

##[0.2.8] - [2016-02-10]
###Fixed
- Changed env update when we eval a program. now we use swap!/merge (we used to erroneously do reset!)

##[0.2.7] - [2016-02-10]
###Fixed
- Fixed a bug in program evaluations which was not recording their
  order properly.

##[0.2.6] - [2016-02-09]
###Added
- ClojureScript support using reader conditionals
- a REPL based on node.js


##[0.2.5] - [2016-02-08]
###Added
- Now you can play around with *scheje* using its little incorporated REPL.

##[0.2.4] - [2016-02-06]
###Added
- *set!*, *named let*, *let**, *letrec(let)*, and *letrec*(let*)* -
  see test files for samples.
- more tests

###Fixed
- Now symbols containing reserved characters can't be used. This fixes
corner cases for macro expansion.

## [0.2.3] - [2016-02-05]
###Added
- Hygienic macro-expansion inspired by the [KFFD algorithm](http://web.cs.ucdavis.edu/~devanbu/teaching/260/kohlbecker.pdf)

## [0.2.2] - [2016-02-03]
###Added
- Exception throwing and handling in evaluation

###Fixed
- Enhanced scoping of symbols to avoid clashes caused by symbols
  introduced by macros.

## [0.2.1] - [2016-02-02]
###Added
- quasiquote, unquote, unquote-splicing, and, or 

###Fixed
- Now vars used in macros are stored in their proper scope, so they
  don't pile up across successive calls to same macros.

## [0.2.0] - [2016-02-01]
### Changed
- Now *scheje* uses eval/apply approach. More maintainable, it can now
  also be used as a ClojureScript library, as it evals lists of
  symbols, not macros as in the 0.1.0 Version. Besides, its usage of a
  dedicated execution environment can make it scale to a proper
  programming language, as it now departs from relying on Clojure
  internals for evaluation, as it was the case when it was using macros.
- *define-syntax* works (more or less) properly. now symbols before
  ellipsis are properly parsed and can be respectively used in
  *syntax-rules* templates. Now, it uses unification to find relevant
  patterns to apply to input, and rules are stored in the execution environment.

## [0.1.0] - [2015-12-11]	

### Added
- First version of the interpreter, using Clojure macros. Mainly a
  working version of the work done as [TNTeam's entry for Clojure Cup 2015](https://github.com/parenode/clojure-cup-2015).

[0.2.1]: https://github.com/turbopape/scheje/compare/master@%7B1day%7D...master
