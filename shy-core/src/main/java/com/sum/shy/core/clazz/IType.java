package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

/**
 * 指的是在IClass中，由代码声明的类型
 * 
 * @author chentao26275
 *
 */
public class IType {

	private String className;
	private String simpleName;
	private String typeName;
	private boolean isPrimitive;
	private boolean isArray;
	private List<IType> genericTypes = new ArrayList<>();
	private boolean isWildcard;
	protected IClass declarer;
	private boolean isNative;

	public boolean isVoid() {
		return void.class.getName().equals(className);
	}

	public boolean isObj() {
		return Object.class.getName().equals(className);
	}

	public boolean isStr() {
		return String.class.getName().equals(className);
	}

	public boolean isList() {
		return List.class.getName().equals(className);
	}

	public boolean isMap() {
		return Map.class.getName().equals(className);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IType) {
			IType type = (IType) obj;
			boolean flag = getClassName().equals(type.getClassName());
			if (flag) {
				int count = 0;
				for (IType genericType : getGenericTypes()) {
					if (!genericType.equals(type.getGenericTypes().get(count++))) {
						flag = false;
						break;
					}
				}
			}
			return flag;
		}
		return false;
	}

	public String build() {

		if (isWildcard())
			return "?";

		String finalName = declarer.addImport(getClassName()) ? getSimpleName() : getTypeName();

		if (isGenericType()) {// 泛型
			List<String> strs = new ArrayList<>();
			for (IType genericType : getGenericTypes())
				strs.add(genericType.build());
			return finalName + "<" + Joiner.on(", ").join(strs) + ">";
		}

		return finalName;
	}

	@Override
	public String toString() {
		throw new RuntimeException("Please use the build method!className:" + getClassName());
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	public void setPrimitive(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public boolean isGenericType() {
		return genericTypes != null && genericTypes.size() > 0;
	}

	public List<IType> getGenericTypes() {
		return genericTypes;
	}

	public void setGenericTypes(List<IType> genericTypes) {
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public void setWildcard(boolean isWildcard) {
		this.isWildcard = isWildcard;
	}

	public IClass getDeclarer() {
		return declarer;
	}

	public void setDeclarer(IClass declarer) {
		this.declarer = declarer;
	}

	public boolean isNative() {
		return isNative;
	}

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

}
