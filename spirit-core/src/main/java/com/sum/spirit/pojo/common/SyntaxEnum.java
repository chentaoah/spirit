package com.sum.spirit.pojo.common;

/**
 * The enum of syntax
 */
public enum SyntaxEnum {
	IMPORT, // import
	ANNOTATION, // @Annotation
	INTERFACE, // interface
	ABSTRACT, // abstract
	CLASS, // class
	FIELD_ASSIGN, // name = "chen"
	FUNC_DECLARE, // String hello() {
	FUNC, // func hello(){
	SUPER, // super()
	THIS, // this()
	DECLARE, // String text
	DECLARE_ASSIGN, // String text = "hello"
	ASSIGN, // text= "hello"
	INVOKE, // var1.invoke()
	RETURN, // return var1
	IF, // if i > 100 {
	ELSEIF, // else if i < 100 {
	ELSE, // else {
	END, // }
	FOR, // for
	FOR_IN, // for item in list {
	WHILE, // while i > 100 {
	CONTINUE, // continue
	BREAK, // break
	TRY, // try {
	CATCH, // catch Exception e {
	FINALLY, // finally {
	THROW, // throw RuntimeException(e)
	SYNC, // sync
	PRINT, // print
	DEBUG, // debug
	ERROR // error
}
