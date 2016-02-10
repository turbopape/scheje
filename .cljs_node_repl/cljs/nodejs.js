// Compiled by ClojureScript 1.7.228 {:target :nodejs}
goog.provide('cljs.nodejs');
goog.require('cljs.core');
cljs.nodejs.require = require;
cljs.nodejs.process = process;
cljs.nodejs.enable_util_print_BANG_ = (function cljs$nodejs$enable_util_print_BANG_(){
cljs.core._STAR_print_newline_STAR_ = false;

cljs.core._STAR_print_fn_STAR_ = (function() { 
var G__32781__delegate = function (args){
return console.log.apply(console,cljs.core.into_array.call(null,args));
};
var G__32781 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__32782__i = 0, G__32782__a = new Array(arguments.length -  0);
while (G__32782__i < G__32782__a.length) {G__32782__a[G__32782__i] = arguments[G__32782__i + 0]; ++G__32782__i;}
  args = new cljs.core.IndexedSeq(G__32782__a,0);
} 
return G__32781__delegate.call(this,args);};
G__32781.cljs$lang$maxFixedArity = 0;
G__32781.cljs$lang$applyTo = (function (arglist__32783){
var args = cljs.core.seq(arglist__32783);
return G__32781__delegate(args);
});
G__32781.cljs$core$IFn$_invoke$arity$variadic = G__32781__delegate;
return G__32781;
})()
;

cljs.core._STAR_print_err_fn_STAR_ = (function() { 
var G__32784__delegate = function (args){
return console.error.apply(console,cljs.core.into_array.call(null,args));
};
var G__32784 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__32785__i = 0, G__32785__a = new Array(arguments.length -  0);
while (G__32785__i < G__32785__a.length) {G__32785__a[G__32785__i] = arguments[G__32785__i + 0]; ++G__32785__i;}
  args = new cljs.core.IndexedSeq(G__32785__a,0);
} 
return G__32784__delegate.call(this,args);};
G__32784.cljs$lang$maxFixedArity = 0;
G__32784.cljs$lang$applyTo = (function (arglist__32786){
var args = cljs.core.seq(arglist__32786);
return G__32784__delegate(args);
});
G__32784.cljs$core$IFn$_invoke$arity$variadic = G__32784__delegate;
return G__32784;
})()
;

return null;
});

//# sourceMappingURL=nodejs.js.map