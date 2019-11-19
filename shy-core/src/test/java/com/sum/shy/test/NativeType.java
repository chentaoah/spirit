package com.sum.shy.test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地类型
 * 
 * @author chentao
 *
 */
public class NativeType {

	public boolean forceFullName = false;// 是否强制使用全名，因为命名可能冲突了

	public Class<?> clazz;// 类名

	public Map<String, NativeType> genericTypes;// List<E> E-String

	public NativeType(Class<?> clazz, Map<String, NativeType> genericTypes) {
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new LinkedHashMap<>() : genericTypes;
	}

	public NativeType(Class<?> clazz) {
		this.clazz = clazz;
		this.genericTypes = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getSimpleName());
		if (genericTypes.size() > 0) {
			sb.append("<");
			for (NativeType nativeType : genericTypes.values()) {
				// 集合里面必须是包装类
				if (nativeType.isPrimitive()) {
					sb.append(nativeType.getWrapper().getSimpleName() + ",");
				} else {
					sb.append(nativeType.toString() + ",");
				}

			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(">");
		}
		return sb.toString();
	}

	public String getSimpleName() {
		return forceFullName ? clazz.getName() : clazz.getSimpleName();
	}

	public String getName() {
		return clazz.getName();
	}

	public Class<?> getWrapper() {
		if (isBoolean()) {
			return Boolean.class;
		} else if (isInt()) {
			return Integer.class;
		} else if (isDouble()) {
			return Double.class;
		}
		return clazz;
	}

	public boolean isPrimitive() {
		return isBoolean() || isInt() || isDouble();
	}

	public boolean isBoolean() {
		return clazz == boolean.class;
	}

	public boolean isInt() {
		return clazz == int.class;
	}

	public boolean isDouble() {
		return clazz == double.class;
	}

	public boolean isStr() {
		return clazz == String.class;
	}

	public boolean isArray() {
		return clazz == List.class;
	}

	public boolean isMap() {
		return clazz == Map.class;
	}

	public boolean isObj() {
		return clazz == Object.class;
	}

	public boolean isVoid() {
		return clazz == void.class;
	}

}