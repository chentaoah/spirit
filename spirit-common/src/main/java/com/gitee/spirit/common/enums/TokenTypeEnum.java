package com.gitee.spirit.common.enums;

public enum TokenTypeEnum {
	ACCESS_PATH, // com.gitee.spirit.example.Animal
	ANNOTATION, // @Animal
	KEYWORD, // class
	OPERATOR, // =
	SEPARATOR, // {
	TYPE, // Horse
	ARRAY_INIT, // Horse[1]
	TYPE_INIT, // Horse()
    TYPE_BUILDER, // Horse{}
	TYPE_SMART_BUILDER, // ${}
	NULL, // null
	BOOLEAN, // true
	CHAR, // 'c'
	INT, // 0
	LONG, // 0L
	DOUBLE, // 0.0
	STRING, // "Jessie"
	LIST, // ["Jessie"]
	MAP, // {"Jessie" : 0}
	SUBEXPRESS, // (x + y)
	CAST, // (Horse)
	VARIABLE, // name
	LOCAL_METHOD, // call()
	VISIT_FIELD, // .name
	VISIT_METHOD, // .call()
	VISIT_INDEX, // [0]
	PREFIX, // .call in .call()
	CUSTOM_PREFIX, // user defined
	CUSTOM_SUFFIX, // user defined
	CUSTOM_EXPRESS // user defined
}
