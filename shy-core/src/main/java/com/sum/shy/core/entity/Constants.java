package com.sum.shy.core.entity;

/**
 * 一些常量
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月30日
 */
public class Constants {

	public static final String STATIC_SCOPE = "static";
	public static final String CLASS_SCOPE = "class";
	public static final String METHOD_SCOPE = "method";

	public static final String UNKNOWN = "unknown";
	public static final String NONE = "none";

	public static final String PACKAGE_SYNTAX = "package";
	public static final String IMPORT_SYNTAX = "import";
	public static final String DEF_SYNTAX = "def";
	public static final String CLASS_SYNTAX = "class";
	public static final String FUNC_SYNTAX = "func";
	public static final String DECLARE_SYNTAX = "declare";
	public static final String ASSIGNMENT_SYNTAX = "assignment";
	public static final String INVOKE_SYNTAX = "invoke";
	public static final String IF_SYNTAX = "if";
	public static final String ELSEIF_SYNTAX = "elseif";
	public static final String ELSE_SYNTAX = "else";
	public static final String END_SYNTAX = "end";

	public static final String KEYWORD_TOKEN = "keyword";
	public static final String KEYWORD_PARAM_TOKEN = "keyword_param";
	public static final String OPERATOR_TOKEN = "operator";
	public static final String SEPARATOR_TOKEN = "separator";
	public static final String NULL_TOKEN = "null";
	public static final String BOOLEAN_TOKEN = "boolean";
	public static final String INT_TOKEN = "int";
	public static final String DOUBLE_TOKEN = "double";
	public static final String STR_TOKEN = "str";
	public static final String ARRAY_TOKEN = "array";
	public static final String MAP_TOKEN = "map";
	public static final String INVOKE_INIT_TOKEN = "invoke_init";
	public static final String INVOKE_STATIC_TOKEN = "invoke_static";
	public static final String INVOKE_MEMBER_TOKEN = "invoke_member";
	public static final String INVOKE_LOCAL_TOKEN = "invoke_local";
	public static final String VAR_TOKEN = "var";
	public static final String STATIC_VAR_TOKEN = "static_var";
	public static final String MEMBER_VAR_TOKEN = "member_var";
	public static final String CAST_TOKEN = "cast";// 类型强制转换
	public static final String INVOKE_FLUENT_TOKEN = "invoke_fluent";// 流式调用 what like ".say()" in "list.get(0).say()"

	// 附加的token
	public static final String PREFIX_TOKEN = "prefix";// 分隔符前面的一串字符，统一称为前缀
	public static final String SUFFIX_TOKEN = "suffix";// 后缀
	public static final String EXPRESS_TOKEN = "express";// 表达式
	public static final String TYPE_TOKEN = "type";

	public static final String TYPE_ATTACHMENT = "type";
	public static final String GENERIC_TYPES_ATTACHMENT = "generic_types";
	public static final String VAR_NAME_ATTACHMENT = "var_name";
	public static final String MEMBER_VAR_NAME_ATTACHMENT = "member_var_name";
	public static final String INIT_METHOD_NAME_ATTACHMENT = "init_method_name";
	public static final String STATIC_METHOD_NAME_ATTACHMENT = "static_method_name";
	public static final String MEMBER_METHOD_NAME_ATTACHMENT = "member_method_name";
	public static final String LOCAL_METHOD_NAME_ATTACHMENT = "local_method_name";
	// 基本类型
	public static final String NULL_TYPE = "null";
	public static final String BOOLEAN_TYPE = "boolean";
	public static final String INT_TYPE = "int";
	public static final String DOUBLE_TYPE = "double";
	public static final String STR_TYPE = "str";
	public static final String ARRAY_TYPE = "array";
	public static final String MAP_TYPE = "map";
	public static final String OBJ_TYPE = "obj";

}
