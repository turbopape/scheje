// Compiled by ClojureScript 1.7.228 {:target :nodejs}
goog.provide('scheje.unifier');
goog.require('cljs.core');
goog.require('cljs.core.match');
goog.require('scheje.tools');
goog.require('clojure.zip');
goog.require('clojure.walk');
scheje.unifier.occurs_QMARK_ = (function scheje$unifier$occurs_QMARK_(s,t){
if(cljs.core.seq_QMARK_.call(null,t)){
var zpr = clojure.zip.zipper.call(null,cljs.core.seq_QMARK_,cljs.core.identity,cljs.core.conj,t);
var loc = clojure.zip.down.call(null,zpr);
while(true){
if(cljs.core.truth_(clojure.zip.end_QMARK_.call(null,loc))){
return false;
} else {
if(cljs.core._EQ_.call(null,s,clojure.zip.node.call(null,loc))){
return true;
} else {
var G__32380 = clojure.zip.next.call(null,loc);
loc = G__32380;
continue;
}
}
break;
}
} else {
return cljs.core._EQ_.call(null,s,t);
}
});
scheje.unifier.s = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
scheje.unifier.iteration_id = cljs.core.atom.call(null,(0));
scheje.unifier.scope = cljs.core.atom.call(null,(0));
scheje.unifier.apply_subst = (function scheje$unifier$apply_subst(u){
if(cljs.core.seq_QMARK_.call(null,u)){
return cljs.core.replace.call(null,cljs.core.deref.call(null,scheje.unifier.s),u);
} else {
var temp__4655__auto__ = cljs.core.get.call(null,cljs.core.deref.call(null,scheje.unifier.s),u);
if(cljs.core.truth_(temp__4655__auto__)){
var subst = temp__4655__auto__;
return subst;
} else {
return u;
}
}
});
scheje.unifier.dynamic_free_symbols = (function scheje$unifier$dynamic_free_symbols(exp,ts,env){
return clojure.walk.postwalk.call(null,(function (x){
if((x instanceof cljs.core.Symbol)){
if(cljs.core.truth_((function (){var or__30848__auto__ = cljs.core.some.call(null,cljs.core.PersistentHashSet.fromArray([x], true),cljs.core.get.call(null,env,new cljs.core.Keyword(null,"keywords","keywords",1526959054)));
if(cljs.core.truth_(or__30848__auto__)){
return or__30848__auto__;
} else {
var or__30848__auto____$1 = cljs.core.some.call(null,cljs.core.PersistentHashSet.fromArray([x], true),cljs.core.map.call(null,new cljs.core.Keyword(null,"name","name",1843675177),cljs.core.get.call(null,env,new cljs.core.Keyword(null,"syntax","syntax",-1637761676))));
if(cljs.core.truth_(or__30848__auto____$1)){
return or__30848__auto____$1;
} else {
return cljs.core.some.call(null,new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, ["#",null], null), null),cljs.core.name.call(null,x));
}
}
})())){
return x;
} else {
return cljs.core.symbol.call(null,[cljs.core.str(cljs.core.name.call(null,x)),cljs.core.str("#"),cljs.core.str(ts)].join(''));

}
} else {
return x;
}
}),exp);
});
scheje.unifier.try_subst = (function scheje$unifier$try_subst(ts,env,u,v,ks,kf){
var u__$1 = scheje.unifier.apply_subst.call(null,u);
if(!((u__$1 instanceof cljs.core.Symbol))){
return ((function (u__$1){
return (function (){
return scheje.unifier.uni.call(null,ts,env,u__$1,v,ks,kf);
});
;})(u__$1))
} else {
var v__$1 = scheje.unifier.apply_subst.call(null,v);
if((cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"_","_",-1201019570,null),u__$1)) || (cljs.core._EQ_.call(null,u__$1,v__$1))){
return ((function (v__$1,u__$1){
return (function (){
return ks.call(null,cljs.core.deref.call(null,scheje.unifier.s));
});
;})(v__$1,u__$1))
} else {
if(cljs.core.truth_(scheje.tools.literal_QMARK_.call(null,u__$1,cljs.core.deref.call(null,scheje.unifier.s)))){
return ((function (v__$1,u__$1){
return (function (){
return kf.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("literal: "),cljs.core.str(u__$1),cljs.core.str(" mismatch - with: "),cljs.core.str(v__$1)].join('')], null));
});
;})(v__$1,u__$1))
} else {
if(cljs.core.truth_(scheje.unifier.occurs_QMARK_.call(null,u__$1,v__$1))){
return ((function (v__$1,u__$1){
return (function (){
return kf.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("Cycle occuring between "),cljs.core.str(u__$1),cljs.core.str(" and "),cljs.core.str(v__$1)].join('')], null));
});
;})(v__$1,u__$1))
} else {
return ((function (v__$1,u__$1){
return (function (){
return ks.call(null,cljs.core.swap_BANG_.call(null,scheje.unifier.s,cljs.core.assoc,u__$1,scheje.unifier.dynamic_free_symbols.call(null,v__$1,ts,env)));
});
;})(v__$1,u__$1))

}
}
}
}
});
scheje.unifier.get_symbol_idx = (function scheje$unifier$get_symbol_idx(s){
var parsed = cljs.core.js__GT_clj.call(null,s.match((new RegExp("#"))));
if((cljs.core.count.call(null,parsed) > (1))){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"sym","sym",-1444860305),cljs.core.first.call(null,parsed),new cljs.core.Keyword(null,"idx","idx",1053688473),parseInt(cljs.core.last.call(null,parsed))], null);
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"sym","sym",-1444860305),cljs.core.first.call(null,parsed),new cljs.core.Keyword(null,"idx","idx",1053688473),(0)], null);
}
});
scheje.unifier.inc_symbols = (function scheje$unifier$inc_symbols(p){
return clojure.walk.postwalk.call(null,(function (x){
if((!(cljs.core._EQ_.call(null,x,new cljs.core.Symbol(null,"...","...",-1926939749,null)))) && ((x instanceof cljs.core.Symbol))){
var sb_idx = scheje.unifier.get_symbol_idx.call(null,cljs.core.name.call(null,x));
return cljs.core.symbol.call(null,[cljs.core.str(new cljs.core.Keyword(null,"sym","sym",-1444860305).cljs$core$IFn$_invoke$arity$1(sb_idx)),cljs.core.str("#"),cljs.core.str(cljs.core.deref.call(null,scheje.unifier.iteration_id))].join(''));
} else {
return x;
}
}),p);
});
scheje.unifier.uni = (function scheje$unifier$uni(ts,env,u,v,ks,kf){
cljs.core.swap_BANG_.call(null,scheje.unifier.iteration_id,cljs.core.inc);

if((u instanceof cljs.core.Symbol)){
return (function (){
return scheje.unifier.try_subst.call(null,ts,env,u,v,ks,kf);
});
} else {
if((cljs.core.seq_QMARK_.call(null,u)) && (cljs.core.seq_QMARK_.call(null,v))){
try{if(((cljs.core.seq_QMARK_.call(null,u)) || (cljs.core.sequential_QMARK_.call(null,u))) && (cljs.core.seq.call(null,u))){
try{var u_tail__32391 = cljs.core.rest.call(null,u);
if(((cljs.core.seq_QMARK_.call(null,u_tail__32391)) || (cljs.core.sequential_QMARK_.call(null,u_tail__32391))) && (cljs.core.seq.call(null,u_tail__32391))){
try{var u_head__32392 = cljs.core.first.call(null,u_tail__32391);
if(cljs.core._EQ_.call(null,u_head__32392,new cljs.core.Symbol(null,"...","...",-1926939749,null))){
var r = cljs.core.rest.call(null,u_tail__32391);
var exp = cljs.core.first.call(null,u);
var parse_ellipsis = ((function (r,exp,u_head__32392,u_tail__32391){
return (function scheje$unifier$uni_$_parse_ellipsis(u__$1,v__$1,r__$1){
if((v__$1 == null)){
return ((function (r,exp,u_head__32392,u_tail__32391){
return (function() { 
var G__32397__delegate = function (_){
return ks.call(null,cljs.core.deref.call(null,scheje.unifier.s));
};
var G__32397 = function (var_args){
var _ = null;
if (arguments.length > 0) {
var G__32398__i = 0, G__32398__a = new Array(arguments.length -  0);
while (G__32398__i < G__32398__a.length) {G__32398__a[G__32398__i] = arguments[G__32398__i + 0]; ++G__32398__i;}
  _ = new cljs.core.IndexedSeq(G__32398__a,0);
} 
return G__32397__delegate.call(this,_);};
G__32397.cljs$lang$maxFixedArity = 0;
G__32397.cljs$lang$applyTo = (function (arglist__32399){
var _ = cljs.core.seq(arglist__32399);
return G__32397__delegate(_);
});
G__32397.cljs$core$IFn$_invoke$arity$variadic = G__32397__delegate;
return G__32397;
})()
;
;})(r,exp,u_head__32392,u_tail__32391))
} else {
return ((function (r,exp,u_head__32392,u_tail__32391){
return (function() { 
var G__32400__delegate = function (_){
var n_exp = scheje.unifier.inc_symbols.call(null,u__$1);
return scheje$unifier$uni.call(null,ts,env,n_exp,cljs.core.first.call(null,v__$1),((function (n_exp,r,exp,u_head__32392,u_tail__32391){
return (function (___$1){
return scheje$unifier$uni_$_parse_ellipsis.call(null,n_exp,cljs.core.next.call(null,v__$1),r__$1);
});})(n_exp,r,exp,u_head__32392,u_tail__32391))
,((cljs.core.seq.call(null,r__$1))?((function (n_exp,r,exp,u_head__32392,u_tail__32391){
return (function() { 
var G__32401__delegate = function (___$1){
return scheje$unifier$uni.call(null,ts,env,r__$1,v__$1,ks,kf);
};
var G__32401 = function (var_args){
var ___$1 = null;
if (arguments.length > 0) {
var G__32402__i = 0, G__32402__a = new Array(arguments.length -  0);
while (G__32402__i < G__32402__a.length) {G__32402__a[G__32402__i] = arguments[G__32402__i + 0]; ++G__32402__i;}
  ___$1 = new cljs.core.IndexedSeq(G__32402__a,0);
} 
return G__32401__delegate.call(this,___$1);};
G__32401.cljs$lang$maxFixedArity = 0;
G__32401.cljs$lang$applyTo = (function (arglist__32403){
var ___$1 = cljs.core.seq(arglist__32403);
return G__32401__delegate(___$1);
});
G__32401.cljs$core$IFn$_invoke$arity$variadic = G__32401__delegate;
return G__32401;
})()
;})(n_exp,r,exp,u_head__32392,u_tail__32391))
:((function (n_exp,r,exp,u_head__32392,u_tail__32391){
return (function() { 
var G__32404__delegate = function (___$1){
return ks.call(null,cljs.core.deref.call(null,scheje.unifier.s));
};
var G__32404 = function (var_args){
var ___$1 = null;
if (arguments.length > 0) {
var G__32405__i = 0, G__32405__a = new Array(arguments.length -  0);
while (G__32405__i < G__32405__a.length) {G__32405__a[G__32405__i] = arguments[G__32405__i + 0]; ++G__32405__i;}
  ___$1 = new cljs.core.IndexedSeq(G__32405__a,0);
} 
return G__32404__delegate.call(this,___$1);};
G__32404.cljs$lang$maxFixedArity = 0;
G__32404.cljs$lang$applyTo = (function (arglist__32406){
var ___$1 = cljs.core.seq(arglist__32406);
return G__32404__delegate(___$1);
});
G__32404.cljs$core$IFn$_invoke$arity$variadic = G__32404__delegate;
return G__32404;
})()
;})(n_exp,r,exp,u_head__32392,u_tail__32391))
));
};
var G__32400 = function (var_args){
var _ = null;
if (arguments.length > 0) {
var G__32407__i = 0, G__32407__a = new Array(arguments.length -  0);
while (G__32407__i < G__32407__a.length) {G__32407__a[G__32407__i] = arguments[G__32407__i + 0]; ++G__32407__i;}
  _ = new cljs.core.IndexedSeq(G__32407__a,0);
} 
return G__32400__delegate.call(this,_);};
G__32400.cljs$lang$maxFixedArity = 0;
G__32400.cljs$lang$applyTo = (function (arglist__32408){
var _ = cljs.core.seq(arglist__32408);
return G__32400__delegate(_);
});
G__32400.cljs$core$IFn$_invoke$arity$variadic = G__32400__delegate;
return G__32400;
})()
;
;})(r,exp,u_head__32392,u_tail__32391))
}
});})(r,exp,u_head__32392,u_tail__32391))
;
return cljs.core.trampoline.call(null,parse_ellipsis,exp,v,r);
} else {
throw cljs.core.match.backtrack;

}
}catch (e32396){if((e32396 instanceof Error)){
var e__4251__auto__ = e32396;
if((e__4251__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__4251__auto__;
}
} else {
throw e32396;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e32395){if((e32395 instanceof Error)){
var e__4251__auto__ = e32395;
if((e__4251__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__4251__auto__;
}
} else {
throw e32395;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e32394){if((e32394 instanceof Error)){
var e__4251__auto__ = e32394;
if((e__4251__auto__ === cljs.core.match.backtrack)){
if(cljs.core.truth_((function (){var or__30848__auto__ = cljs.core._EQ_.call(null,cljs.core.count.call(null,u),cljs.core.count.call(null,v));
if(or__30848__auto__){
return or__30848__auto__;
} else {
return cljs.core.some.call(null,new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Symbol(null,"...","...",-1926939749,null),null], null), null),u);
}
})())){
var internal_symbols = ((function (e__4251__auto__){
return (function scheje$unifier$uni_$_internal_symbols(u__$1,v__$1){
if((u__$1 == null)){
return ((function (e__4251__auto__){
return (function (){
return ks.call(null,cljs.core.deref.call(null,scheje.unifier.s));
});
;})(e__4251__auto__))
} else {
return ((function (e__4251__auto__){
return (function (){
if(cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"...","...",-1926939749,null),cljs.core.second.call(null,u__$1))){
return scheje$unifier$uni.call(null,ts,env,u__$1,v__$1,((function (e__4251__auto__){
return (function (_){
return scheje$unifier$uni_$_internal_symbols.call(null,cljs.core.next.call(null,u__$1),cljs.core.next.call(null,v__$1));
});})(e__4251__auto__))
,kf);
} else {
if(cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"...","...",-1926939749,null),cljs.core.first.call(null,u__$1))){
return ((function (e__4251__auto__){
return (function (){
return ks.call(null,cljs.core.deref.call(null,scheje.unifier.s));
});
;})(e__4251__auto__))
} else {
return scheje$unifier$uni.call(null,ts,env,cljs.core.first.call(null,u__$1),cljs.core.first.call(null,v__$1),((function (e__4251__auto__){
return (function (_){
return scheje$unifier$uni_$_internal_symbols.call(null,cljs.core.next.call(null,u__$1),cljs.core.next.call(null,v__$1));
});})(e__4251__auto__))
,kf);

}
}
});
;})(e__4251__auto__))
}
});})(e__4251__auto__))
;
return cljs.core.trampoline.call(null,internal_symbols,u,v);
} else {
return ((function (e__4251__auto__){
return (function (){
return kf.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("different length in "),cljs.core.str(u),cljs.core.str("and "),cljs.core.str(v)].join('')], null));
});
;})(e__4251__auto__))

}
} else {
throw e__4251__auto__;
}
} else {
throw e32394;

}
}} else {
if(cljs.core._EQ_.call(null,u,v)){
return (function (){
return ks.call(null,cljs.core.deref.call(null,scheje.unifier.s));
});
} else {
return (function (){
return kf.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("Clash in "),cljs.core.str(u),cljs.core.str(" and "),cljs.core.str(v)].join('')], null));
});
}

}
}
});
scheje.unifier.unify = (function scheje$unifier$unify(u,v,ts,env){
var _ = cljs.core.reset_BANG_.call(null,scheje.unifier.s,cljs.core.PersistentArrayMap.EMPTY);
var ___$1 = cljs.core.reset_BANG_.call(null,scheje.unifier.iteration_id,(0));
return cljs.core.trampoline.call(null,scheje.unifier.uni,ts,env,u,v,cljs.core.identity,cljs.core.identity);
});

//# sourceMappingURL=unifier.js.map