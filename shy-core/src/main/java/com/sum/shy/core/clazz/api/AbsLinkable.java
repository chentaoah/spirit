package com.sum.shy.core.clazz.api;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbsLinkable extends AbsAnnotated implements Linkable {
	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new LinkedHashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new LinkedHashMap<>();

	public String getPackage() {
		return packageStr;
	}

}
