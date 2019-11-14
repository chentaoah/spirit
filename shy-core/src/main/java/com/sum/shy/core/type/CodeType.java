package com.sum.shy.core.type;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Stmt;

public class CodeType implements Type {

	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
	public static final Pattern ARRAY_TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+<[a-zA-Z0-9<>\\.]+>$");

	public String text;// 文本

	public String className;// 类名

	public String type;// 普通,数组,泛型

	public Map<String, CodeType> genericTypes = new LinkedHashMap<>();// 泛型参数

	/**
	 * 根据代码生成type,但是这个也只是个字符串,并无实际类型
	 * 
	 * @param clazz
	 * @param type
	 */
	public CodeType(Clazz clazz, String type) {
		// 文本
		this.text = type;

		if (TYPE_PATTERN.matcher(type).matches()) {
			this.className = findImport(clazz, type);
			this.type = "type";

		} else if (ARRAY_TYPE_PATTERN.matcher(type).matches()) {
			this.className = findImport(clazz, type.substring(0, type.indexOf("[")));
			this.type = "array_type";

		} else if (GENERIC_TYPE_PATTERN.matcher(type).matches()) {
			this.className = findImport(clazz, type.substring(0, type.indexOf("<")));
			this.type = "generic_type";
			getGenericTypes(type);
		}

	}

	/**
	 * 基本类型
	 * 
	 * @param type
	 */
	public CodeType(String type) {

	}

	public String findImport(Clazz clazz, String type) {
		// 查询一下完整的类名
		String className = clazz.findImport(type);
		// 从全局代码中寻找该类
		if (className == null) {
			className = Context.get().findImport(type);
		}
		return className;
	}

	public void getGenericTypes(String type) {
		Stmt stmt = Stmt.create(type);

	}

	public boolean isType() {
		return "type".equals(type);
	}

	public boolean isArrayType() {
		return "array_type".equals(type);
	}

	public boolean isGenericType() {
		return "generic_type".equals(type);
	}

}
