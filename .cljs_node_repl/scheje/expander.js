// Compiled by ClojureScript 1.7.228 {:target :nodejs}
goog.provide('scheje.expander');
goog.require('cljs.core');
goog.require('scheje.unifier');
goog.require('scheje.tools');
goog.require('clojure.walk');
/**
 * returns all symbols beginning with s, ordered 
 */
scheje.expander.get_ordered_symbols_for = (function scheje$expander$get_ordered_symbols_for(s,a_match){
return cljs.core.sort_by.call(null,cljs.core.comp.call(null,new cljs.core.Keyword(null,"idx","idx",1053688473),scheje.unifier.get_symbol_idx,cljs.core.str),cljs.core._GT_,cljs.core.map.call(null,(function (p1__32615_SHARP_){
return cljs.core.get.call(null,p1__32615_SHARP_,(0));
}),cljs.core.filter.call(null,cljs.core.comp.call(null,cljs.core.partial.call(null,cljs.core._EQ_,[cljs.core.str(s)].join('')),new cljs.core.Keyword(null,"sym","sym",-1444860305),scheje.unifier.get_symbol_idx,cljs.core.str,cljs.core.key),a_match)));
});
scheje.expander.get_exp_symbols = (function scheje$expander$get_exp_symbols(exp,a_match_symbols){
var res = cljs.core.atom.call(null,cljs.core.List.EMPTY);
clojure.walk.postwalk.call(null,((function (res){
return (function (x){
if(((x instanceof cljs.core.Symbol)) && (cljs.core.contains_QMARK_.call(null,a_match_symbols,x))){
return cljs.core.swap_BANG_.call(null,res,cljs.core.conj,x);
} else {
return null;
}
});})(res))
,exp);

return cljs.core.deref.call(null,res);
});
scheje.expander.generate_exp = (function scheje$expander$generate_exp(exp,some_matches){
return clojure.walk.postwalk.call(null,(function (x){
if((x instanceof cljs.core.Symbol)){
var temp__4655__auto__ = cljs.core.first.call(null,cljs.core.filter.call(null,(function (p1__32616_SHARP_){
return cljs.core._EQ_.call(null,[cljs.core.str(x)].join(''),new cljs.core.Keyword(null,"sym","sym",-1444860305).cljs$core$IFn$_invoke$arity$1(scheje.unifier.get_symbol_idx.call(null,[cljs.core.str(p1__32616_SHARP_)].join(''))));
}),some_matches));
if(cljs.core.truth_(temp__4655__auto__)){
var the_sym = temp__4655__auto__;
return the_sym;
} else {
return x;
}
} else {
return x;
}
}),exp);
});
scheje.expander.repeat_exp = (function scheje$expander$repeat_exp(exp,a_match){
var syms = scheje.expander.get_exp_symbols.call(null,exp,cljs.core.set.call(null,cljs.core.map.call(null,cljs.core.comp.call(null,cljs.core.symbol,new cljs.core.Keyword(null,"sym","sym",-1444860305),scheje.unifier.get_symbol_idx,cljs.core.str,cljs.core.key),a_match)));
var exp_matches = cljs.core.mapv.call(null,((function (syms){
return (function (p1__32617_SHARP_){
return scheje.expander.get_ordered_symbols_for.call(null,p1__32617_SHARP_,a_match);
});})(syms))
,syms);
var can_expand_QMARK_ = cljs.core.apply.call(null,cljs.core._EQ_,cljs.core.mapv.call(null,cljs.core.count,exp_matches));
if(cljs.core.truth_(can_expand_QMARK_)){
var remaining = exp_matches;
var res = cljs.core.List.EMPTY;
while(true){
if(cljs.core.every_QMARK_.call(null,cljs.core.nil_QMARK_,cljs.core.map.call(null,cljs.core.seq,remaining))){
return res;
} else {
var G__32618 = cljs.core.map.call(null,cljs.core.rest,remaining);
var G__32619 = cljs.core.conj.call(null,res,scheje.expander.generate_exp.call(null,exp,cljs.core.map.call(null,cljs.core.first,remaining)));
remaining = G__32618;
res = G__32619;
continue;
}
break;
}
} else {
return cljs.core.list(new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Symbol(null,"in","in",109346662,null),new cljs.core.Symbol(null,"symbol","symbol",601958831,null),new cljs.core.Symbol(null,"eval","eval",536963622,null),new cljs.core.Symbol(null,"for","for",316745208,null),new cljs.core.Symbol(null,"some","some",-310548046,null),new cljs.core.Symbol(null,"ellipsis","ellipsis",-1655930031,null));
}
});
scheje.expander.expand_exp = (function scheje$expander$expand_exp(exp,a_match){
if(cljs.core.truth_(scheje.tools.atom_QMARK_.call(null,exp))){
return exp;
} else {
if(cljs.core.not.call(null,cljs.core.seq.call(null,exp))){
return cljs.core.List.EMPTY;
} else {
if(cljs.core.seq_QMARK_.call(null,exp)){
if(cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"...","...",-1926939749,null),cljs.core.second.call(null,exp))){
return cljs.core.into.call(null,scheje$expander$expand_exp.call(null,cljs.core.rest.call(null,cljs.core.rest.call(null,exp)),a_match),cljs.core.reverse.call(null,scheje.expander.repeat_exp.call(null,cljs.core.first.call(null,exp),a_match)));
} else {
return cljs.core.cons.call(null,scheje$expander$expand_exp.call(null,cljs.core.first.call(null,exp),a_match),scheje$expander$expand_exp.call(null,cljs.core.rest.call(null,exp),a_match));
}
} else {
return null;
}
}
}
});
scheje.expander.expand_w_bindings = (function scheje$expander$expand_w_bindings(exp,a_match){
var expanded = scheje.expander.expand_exp.call(null,exp,a_match);
return clojure.walk.postwalk.call(null,((function (expanded){
return (function (x){
var bdng = cljs.core.get.call(null,a_match,x);
if(!((bdng == null))){
return bdng;
} else {
return x;
}
});})(expanded))
,expanded);
});

//# sourceMappingURL=expander.js.map