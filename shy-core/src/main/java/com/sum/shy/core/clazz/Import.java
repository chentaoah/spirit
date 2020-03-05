package com.sum.shy.core.clazz;

import com.sum.shy.core.document.Element;
import com.sum.shy.core.utils.TypeUtils;

public class Import {

	public String className;

	public String name;

	public String alias;

	public Import(Element element) {
		className = element.getStr(1);
		name = TypeUtils.getTypeNameByClassName(className);
		if (element.contains(2))
			alias = element.getStr(2);// 如果有别名，则使用别名
	}

	public Import(String className, String name) {
		this.className = className;
		this.name = name;
	}

	public boolean hasAlias() {
		return alias != null;
	}

	public boolean isMatch(String typeName) {
		if (alias != null) {// 如果有别名，则以别名的匹配结果为准
			return alias.equals(typeName);
		} else if (name != null) {
			return name.equals(typeName);
		}
		return false;
	}

}
