package com.sum.spirit.pojo.common;

import java.lang.reflect.Modifier;

public class Constants {

	public static final String PACKAGE_KEYWORD = "package";// 高频出现的几个关键字
	public static final String IMPORT_KEYWORD = "import";
	public static final String INTERFACE_KEYWORD = "interface";
	public static final String ABSTRACT_KEYWORD = "abstract";
	public static final String CLASS_KEYWORD = "class";
	public static final String EXTENDS_KEYWORD = "extends";
	public static final String IMPLS_KEYWORD = "impls";
	public static final String PUBLIC_KEYWORD = "public";
	public static final String STATIC_KEYWORD = "static";
	public static final String SYNCH_KEYWORD = "synch";
	public static final String CONST_KEYWORD = "const";
	public static final String FUNC_KEYWORD = "func";
	public static final String THROWS_KEYWORD = "throws";
	public static final String SUPER_KEYWORD = "super";
	public static final String THIS_KEYWORD = "this";
	public static final String IF_KEYWORD = "if";
	public static final String ELSE_KEYWORD = "else";
	public static final String FOR_KEYWORD = "for";
	public static final String IN_KEYWORD = "in";
	public static final String CATCH_KEYWORD = "catch";
	public static final String FINALLY_KEYWORD = "finally";
	public static final String SYNC_KEYWORD = "sync";
	public static final String PRINT_KEYWORD = "print";
	public static final String DEBUG_KEYWORD = "debug";
	public static final String ERROR_KEYWORD = "error";
	public static final String INSTANCEOF_KEYWORD = "instanceof";
	public static final String SYNCHRONIZED_KEYWORD = "synchronized";
	public static final String IMPLEMENTS_KEYWORD = "implements";
	public static final String FINAL_KEYWORD = "final";

	public static final String PATH_TOKEN = "path";// 类访问全路径
	public static final String ANNOTATION_TOKEN = "annotation";// 数组或者集合的快速索引
	public static final String KEYWORD_TOKEN = "keyword";
	public static final String OPERATOR_TOKEN = "operator";
	public static final String SEPARATOR_TOKEN = "separator";
	public static final String TYPE_TOKEN = "type";// 类型声明
	public static final String ARRAY_INIT_TOKEN = "array_init";// 数组初始化
	public static final String TYPE_INIT_TOKEN = "type_init";
	public static final String NULL_TOKEN = "null";
	public static final String BOOL_TOKEN = "bool";
	public static final String CHAR_TOKEN = "char";
	public static final String INT_TOKEN = "int";
	public static final String LONG_TOKEN = "long";
	public static final String DOUBLE_TOKEN = "double";
	public static final String STR_TOKEN = "str";
	public static final String LIST_TOKEN = "list";
	public static final String MAP_TOKEN = "map";
	public static final String SUBEXPRESS_TOKEN = "subexpress";// 子表达式
	public static final String CAST_TOKEN = "cast";// 类型强制转换
	public static final String VAR_TOKEN = "var";
	public static final String LOCAL_METHOD_TOKEN = "local_method";// 本地方法
	public static final String VISIT_FIELD_TOKEN = "visit_field";// 访问字段
	public static final String INVOKE_METHOD_TOKEN = "invoke_method";// 调用方法
	public static final String VISIT_ARRAY_INDEX_TOKEN = "visit_array_index";// 访问字段,并通过索引访问数组中的元素
	public static final String ARRAY_INDEX_TOKEN = "array_index";// 访问字段,并通过索引访问数组中的元素
	public static final String PREFIX_TOKEN = "prefix";// 前缀
	public static final String NODE_TOKEN = "node";// 节点
	public static final String CUSTOM_PREFIX_TOKEN = "custom_prefix";// 自定义前缀
	public static final String CUSTOM_SUFFIX_TOKEN = "custom_suffix";// 自定义后缀
	public static final String CUSTOM_EXPRESS_TOKEN = "custom_express";// 自定义表达式

	public static final int THIS_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;
	public static final int SUPER_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED;
	public static final int PUBLIC_MODIFIERS = Modifier.PUBLIC;

	public static final String ARRAY_LENGTH = "length";

}
