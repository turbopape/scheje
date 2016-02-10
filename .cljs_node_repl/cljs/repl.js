// Compiled by ClojureScript 1.7.228 {:target :nodejs}
goog.provide('cljs.repl');
goog.require('cljs.core');
cljs.repl.print_doc = (function cljs$repl$print_doc(m){
cljs.core.println.call(null,"-------------------------");

cljs.core.println.call(null,[cljs.core.str((function (){var temp__4657__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__4657__auto__)){
var ns = temp__4657__auto__;
return [cljs.core.str(ns),cljs.core.str("/")].join('');
} else {
return null;
}
})()),cljs.core.str(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Protocol");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__32004_32018 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__32005_32019 = null;
var count__32006_32020 = (0);
var i__32007_32021 = (0);
while(true){
if((i__32007_32021 < count__32006_32020)){
var f_32022 = cljs.core._nth.call(null,chunk__32005_32019,i__32007_32021);
cljs.core.println.call(null,"  ",f_32022);

var G__32023 = seq__32004_32018;
var G__32024 = chunk__32005_32019;
var G__32025 = count__32006_32020;
var G__32026 = (i__32007_32021 + (1));
seq__32004_32018 = G__32023;
chunk__32005_32019 = G__32024;
count__32006_32020 = G__32025;
i__32007_32021 = G__32026;
continue;
} else {
var temp__4657__auto___32027 = cljs.core.seq.call(null,seq__32004_32018);
if(temp__4657__auto___32027){
var seq__32004_32028__$1 = temp__4657__auto___32027;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__32004_32028__$1)){
var c__31651__auto___32029 = cljs.core.chunk_first.call(null,seq__32004_32028__$1);
var G__32030 = cljs.core.chunk_rest.call(null,seq__32004_32028__$1);
var G__32031 = c__31651__auto___32029;
var G__32032 = cljs.core.count.call(null,c__31651__auto___32029);
var G__32033 = (0);
seq__32004_32018 = G__32030;
chunk__32005_32019 = G__32031;
count__32006_32020 = G__32032;
i__32007_32021 = G__32033;
continue;
} else {
var f_32034 = cljs.core.first.call(null,seq__32004_32028__$1);
cljs.core.println.call(null,"  ",f_32034);

var G__32035 = cljs.core.next.call(null,seq__32004_32028__$1);
var G__32036 = null;
var G__32037 = (0);
var G__32038 = (0);
seq__32004_32018 = G__32035;
chunk__32005_32019 = G__32036;
count__32006_32020 = G__32037;
i__32007_32021 = G__32038;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_32039 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__30848__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__30848__auto__)){
return or__30848__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.call(null,arglists_32039);
} else {
cljs.core.prn.call(null,((cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first.call(null,arglists_32039)))?cljs.core.second.call(null,arglists_32039):arglists_32039));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Special Form");

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.contains_QMARK_.call(null,m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.call(null,[cljs.core.str("\n  Please see http://clojure.org/"),cljs.core.str(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))].join(''));
} else {
return null;
}
} else {
return cljs.core.println.call(null,[cljs.core.str("\n  Please see http://clojure.org/special_forms#"),cljs.core.str(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Macro");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"REPL Special Function");
} else {
}

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__32008 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__32009 = null;
var count__32010 = (0);
var i__32011 = (0);
while(true){
if((i__32011 < count__32010)){
var vec__32012 = cljs.core._nth.call(null,chunk__32009,i__32011);
var name = cljs.core.nth.call(null,vec__32012,(0),null);
var map__32013 = cljs.core.nth.call(null,vec__32012,(1),null);
var map__32013__$1 = ((((!((map__32013 == null)))?((((map__32013.cljs$lang$protocol_mask$partition0$ & (64))) || (map__32013.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__32013):map__32013);
var doc = cljs.core.get.call(null,map__32013__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists = cljs.core.get.call(null,map__32013__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name);

cljs.core.println.call(null," ",arglists);

if(cljs.core.truth_(doc)){
cljs.core.println.call(null," ",doc);
} else {
}

var G__32040 = seq__32008;
var G__32041 = chunk__32009;
var G__32042 = count__32010;
var G__32043 = (i__32011 + (1));
seq__32008 = G__32040;
chunk__32009 = G__32041;
count__32010 = G__32042;
i__32011 = G__32043;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__32008);
if(temp__4657__auto__){
var seq__32008__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__32008__$1)){
var c__31651__auto__ = cljs.core.chunk_first.call(null,seq__32008__$1);
var G__32044 = cljs.core.chunk_rest.call(null,seq__32008__$1);
var G__32045 = c__31651__auto__;
var G__32046 = cljs.core.count.call(null,c__31651__auto__);
var G__32047 = (0);
seq__32008 = G__32044;
chunk__32009 = G__32045;
count__32010 = G__32046;
i__32011 = G__32047;
continue;
} else {
var vec__32015 = cljs.core.first.call(null,seq__32008__$1);
var name = cljs.core.nth.call(null,vec__32015,(0),null);
var map__32016 = cljs.core.nth.call(null,vec__32015,(1),null);
var map__32016__$1 = ((((!((map__32016 == null)))?((((map__32016.cljs$lang$protocol_mask$partition0$ & (64))) || (map__32016.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__32016):map__32016);
var doc = cljs.core.get.call(null,map__32016__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists = cljs.core.get.call(null,map__32016__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name);

cljs.core.println.call(null," ",arglists);

if(cljs.core.truth_(doc)){
cljs.core.println.call(null," ",doc);
} else {
}

var G__32048 = cljs.core.next.call(null,seq__32008__$1);
var G__32049 = null;
var G__32050 = (0);
var G__32051 = (0);
seq__32008 = G__32048;
chunk__32009 = G__32049;
count__32010 = G__32050;
i__32011 = G__32051;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
}
});

//# sourceMappingURL=repl.js.map