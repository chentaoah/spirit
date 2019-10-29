package com.sum.shy.core.entity;

import java.util.Map;

public class Token {

	public String type;

	public Object value;

	public Map<String, String> attachments;// 解析获得的其他信息

	public Token(String type, Object value, Map<String, String> attachments) {
		this.type = type;
		this.value = value;
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "<" + type + "," + value + ">";
	}

}
