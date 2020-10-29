package com.sum.spirit.pojo.element;

import java.util.HashMap;
import java.util.Map;

import com.sum.spirit.pojo.enums.AttributeEnum;

public abstract class AttributeMap {

	public Map<AttributeEnum, Object> attributes = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(AttributeEnum attributeEnum) {
		return (T) attributes.get(attributeEnum);
	}

	public boolean getAttribute(AttributeEnum attributeEnum, boolean defaultValue) {
		return (boolean) attributes.getOrDefault(attributeEnum, defaultValue);
	}

	public void setAttribute(AttributeEnum attributeEnum, Object value) {
		attributes.put(attributeEnum, value);
	}

}
