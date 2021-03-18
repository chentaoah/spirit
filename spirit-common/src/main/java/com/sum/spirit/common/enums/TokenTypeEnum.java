package com.sum.spirit.common.enums;

public enum TokenTypeEnum {
	PATH, // 类访问全路径
	ANNOTATION, // 注解
	KEYWORD, // 关键字
	OPERATOR, // 操作符
	SEPARATOR, // 分隔符
	TYPE, // 类型声明
	ARRAY_INIT, // 数组初始化
	TYPE_INIT, // 类型初始化
	NULL, // 空
	BOOLEAN, // 布尔
	CHAR, // 字符
	INT, // 整型
	LONG, // 长整型
	DOUBLE, // 双精度浮点数
	STRING, // 字符串
	LIST, // 数组集合
	MAP, // 映射集合
	SUBEXPRESS, // 子表达式
	CAST, // 类型强制转换
	VARIABLE, // 变量
	LOCAL_METHOD, // 本地方法
	VISIT_FIELD, // 访问字段
	VISIT_METHOD, // 调用方法
	VISIT_INDEX, // 访问数组
	PREFIX, // 前缀
	CUSTOM_PREFIX, // 自定义前缀
	CUSTOM_SUFFIX, // 自定义后缀
	CUSTOM_EXPRESS // 自定义表达式
}
