# Change Log
##[0.2.4] - [2016-02-06]
###Added
- *set!*, *named let*, *let**, *letrec(let)*, and *letrec*(let*)* -
  see test files for samples.
- more tests

###Fixed

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
