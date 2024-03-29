package com.gitee.spirit.common.enums;

public enum SyntaxEnum {
	IMPORT, // import com.gitee.spirit.example.Animal
	ANNOTATION, // @Animal
	INTERFACE, // interface Horse {
	ABSTRACT, // abstract Horse {
	CLASS, // class Horse {
	SUPER, // super()
	THIS, // this()
	DECLARE, // String name
	DECLARE_ASSIGN, // String name = "Jessie"
	ASSIGN, // name = "Jessie"
	DECLARE_FUNC, // String call() {
	FUNC, // func call() {
	FIELD_ASSIGN, // horse.name = "Jessie"
	STRUCT_ASSIGN, // horse = {
	INVOKE, // horse.call()
	RETURN, // return "I am Jessie!"
	IF, // if name == "Jessie" {
	ELSE_IF, // } else if name == "Jessie" {
	ELSE, // } else {
	END, // }
	FOR, // for (i=0; i<100; i++) {
	FOR_IN, // for item in list {
	WHILE, // while name == "Jessie" {
	CONTINUE, // continue
	BREAK, // break
	TRY, // try {
	CATCH, // } catch Exception e {
	FINALLY, // } finally {
	THROW, // throw RuntimeException(e)
	SYNC, // sync horse {
	PRINT, // print "Jessie"
	DEBUG, // debug "Jessie"
	ERROR // error "Jessie"
}
