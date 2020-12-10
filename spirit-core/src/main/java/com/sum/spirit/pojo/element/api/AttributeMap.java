package com.sum.spirit.pojo.element.api;

import java.util.Map;

import com.sum.spirit.pojo.enums.AttributeEnum;

public abstract class AttributeMap {

	public Map<AttributeEnum, Object> attributes;

	public AttributeMap(Map<AttributeEnum, Object> attributes) {
		this.attributes = attributes;
	}

	@SuppressWarnings("unchecked")
	public <T> T attr(AttributeEnum attributeEnum) {
		return (T) attributes.get(attributeEnum);
	}

	public boolean attr(AttributeEnum attributeEnum, boolean defaultValue) {
		return (boolean) attributes.getOrDefault(attributeEnum, defaultValue);
	}

	public void setAttr(AttributeEnum attributeEnum, Object value) {
		attributes.put(attributeEnum, value);
	}

}
