package com.sum.shy.core.clazz;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbsLinkable extends AbsAnnotated {

	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new LinkedHashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new LinkedHashMap<>();

}
