package com.sum.shy.core.api;

public interface Type {

	public String getName();

	public boolean isBool();

	public boolean isInt();

	public boolean isLong();

	public boolean isDouble();

	public boolean isStr();

	public boolean isObj();

	public boolean isVoid();

	public boolean isClass();

	public boolean isArray();

	public boolean isMap();

	public boolean isGenericClass();

}
