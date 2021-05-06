package com.sum.spirit.core.element.frame;

import java.util.Map;

public abstract class Attributes {

	public Map<String, Object> attributes;

	public Attributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@SuppressWarnings("unchecked")
	public <T> T attr(String attribute) {
		return (T) attributes.get(attribute);
	}

	public boolean attr(String attribute, boolean defaultValue) {
		return (boolean) attributes.getOrDefault(attribute, defaultValue);
	}

	public void setAttr(String attribute, Object value) {
		attributes.put(attribute, value);
	}

}
