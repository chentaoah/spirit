package com.sum.shy.core.clazz.api;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 可描述的
 * 
 * @author chentao26275
 *
 */
public abstract class AbsDescribable extends AbsAnnotated implements Locable {

	public Map<String, String> desc = new LinkedHashMap<>();

	public String getScope() {
		return desc.get("SCOPE");
	}

	public void setScope(String scope) {
		desc.put("SCOPE", scope);
	}

}
