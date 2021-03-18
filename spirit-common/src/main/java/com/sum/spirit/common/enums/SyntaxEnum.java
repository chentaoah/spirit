package com.sum.spirit.common.enums;

public enum SyntaxEnum {
	IMPORT, // import
	ANNOTATION, // @Annotation
	INTERFACE, // interface
	ABSTRACT, // abstract
	CLASS, // class
	SUPER, // super()
	THIS, // this()
	DECLARE, // String text
	DECLARE_ASSIGN, // String text = "hello"
	ASSIGN, // text= "hello"
	DECLARE_FUNC, // String hello() {
	FUNC, // func hello(){
	FIELD_ASSIGN, // var.text = "abc"
	INVOKE, // var1.invoke()
	RETURN, // return var1
	IF, // if i > 100 {
	ELSE_IF, // else if i < 100 {
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
