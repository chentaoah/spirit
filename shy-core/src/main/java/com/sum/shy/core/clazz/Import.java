package com.sum.shy.core.clazz;

import com.sum.shy.core.doc.Element;
import com.sum.shy.core.utils.TypeUtils;

public class Import {

	public String className;

	public String name;

	public Import(Element element) {
		className = element.getStr(1);
		if (!element.contain(2)) {
			name = TypeUtils.getTypeNameByClass(className);
		} else {
			name = element.getStr(2);// 如果有别名，则使用别名
		}
	}

}
