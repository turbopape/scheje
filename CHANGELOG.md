# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [0.2.0][2016-02-01]
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

## 0.1.0 - 2015-12-11	

### Added
- First version of the interpreter, using Clojure macros. Mainly a
  working version of the work done as [TNTeam's entry for Clojure Cup 2015](https://github.com/parenode/clojure-cup-2015).

[0.2.0]: https://github.com/turbopape/scheje/compare/0.1.0...0.2.0
