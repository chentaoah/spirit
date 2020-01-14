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

	public static final String UNKNOWN = "unknown";

	public static final String STATIC_SCOPE = "static";
	public static final String CLASS_SCOPE = "class";
	public static final String METHOD_SCOPE = "method";

	public static final String INTERFACE_KEYWORD = "interface";// 高频出现的几个关键字
	public static final String ABSTRACT_KEYWORD = "abstract";
	public static final String CLASS_KEYWORD = "class";
	public static final String EXTENDS_KEYWORD = "extends";
	public static final String IMPL_KEYWORD = "impl";

	public static final String IMPORT_SYNTAX = "import";
	public static final String ANNOTATION_SYNTAX = "annotation";
	public static final String INTERFACE_SYNTAX = "interface";
	public static final String ABSTRACT_SYNTAX = "abstract";
	public static final String CLASS_SYNTAX = "class";
	public static final String FUNC_SYNTAX = "func";
	public static final String SUPER_SYNTAX = "super";// 父类构造
	public static final String THIS_SYNTAX = "this";// 调用自身构造
	public static final String DECLARE_SYNTAX = "declare";
	public static final String FUNC_DECLARE_SYNTAX = "func_declare";// 方法声明,在接口中
	public static final String ASSIGN_SYNTAX = "assign";
	public static final String FIELD_ASSIGN_SYNTAX = "field_assign";// 字段赋值
	public static final String INVOKE_SYNTAX = "invoke";
	public static final String RETURN_SYNTAX = "return";
	public static final String IF_SYNTAX = "if";
	public static final String ELSEIF_SYNTAX = "elseif";
	public static final String ELSE_SYNTAX = "else";
	public static final String END_SYNTAX = "end";
	public static final String CATCH_SYNTAX = "catch";
	public static final String FOR_IN_SYNTAX = "for_in";
	public static final String FOR_SYNTAX = "for";// 原生java的语法
	public static final String WHILE_SYNTAX = "while";
	public static final String FAST_ADD_SYNTAX = "fast_add";// 快速添加语法
	public static final String JUDGE_INVOKE_SYNTAX = "judge_invoke";// 判空调用

	public static final String ANNOTATION_TOKEN = "annotation";// 数组或者集合的快速索引
	public static final String KEYWORD_TOKEN = "keyword";
	public static final String KEYWORD_PARAM_TOKEN = "keyword_param";
	public static final String OPERATOR_TOKEN = "operator";
	public static final String SEPARATOR_TOKEN = "separator";
	public static final String TYPE_TOKEN = "type";// 类型声明
	public static final String ARRAY_INIT_TOKEN = "array_init";// 数组初始化
	public static final String TYPE_INIT_TOKEN = "type_init";
	public static final String NULL_TOKEN = "null";
	public static final String BOOL_TOKEN = "bool";
	public static final String INT_TOKEN = "int";
	public static final String LONG_TOKEN = "long";
	public static final String DOUBLE_TOKEN = "double";
	public static final String STR_TOKEN = "str";
	public static final String LIST_TOKEN = "list";
	public static final String MAP_TOKEN = "map";
	public static final String VAR_TOKEN = "var";
	public static final String SUBEXPRESS_TOKEN = "subexpress";// 子表达式
	public static final String CAST_TOKEN = "cast";// 类型强制转换
	public static final String INVOKE_LOCAL_TOKEN = "invoke_local";
	public static final String VISIT_FIELD_TOKEN = "visit_field";
	public static final String INVOKE_METHOD_TOKEN = "invoke_method";
	public static final String VISIT_ARRAY_INDEX_TOKEN = "visit_array_index";// 访问字段,并通过索引访问数组中的元素
	public static final String ARRAY_INDEX_TOKEN = "array_index";// 访问字段,并通过索引访问数组中的元素
	public static final String NODE_TOKEN = "node";// 节点
	public static final String PREFIX_TOKEN = "prefix";// 前缀

	public static final String TYPE_ATTACHMENT = "type";// 变量类型
	public static final String TYPE_NAME_ATTACHMENT = "type_name";// 类名
	public static final String MEMBER_NAME_ATTACHMENT = "member_name";// 成员名
	public static final String IS_DECLARED_ATTACHMENT = "is_declared";// 是否被声明标志
	public static final String POSITION_ATTACHMENT = "position";// 位置

	public static final String CUSTOM_PREFIX_TOKEN = "custom_prefix";// 自定义前缀
	public static final String CUSTOM_SUFFIX_TOKEN = "custom_suffix";// 自定义后缀
	public static final String CUSTOM_EXPRESS_TOKEN = "custom_express";// 自定义表达式

}
