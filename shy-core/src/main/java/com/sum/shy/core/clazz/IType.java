package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.clazz.type.TypeFactory;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.core.utils.TypeUtils;

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
	private boolean isNull;// 是否空值
	private boolean isWildcard;
	private boolean isNative;

	public String getTargetName() {// 返回真正的className,包括数组中的
		return TypeUtils.getTargetName(getClassName());
	}

	public IType getSuperType() {

		if (isPrimitive())
			return null;

		if (isArray())
			return StaticType.OBJECT_TYPE;

		if (!isNative()) {
			IClass clazz = Context.get().findClass(getTargetName());
			return clazz.getSuperType();

		} else {
			Class<?> clazz = ReflectUtils.getClass(getTargetName());
			Class<?> superClass = clazz.getSuperclass();
			return superClass != null ? TypeFactory.create(superClass) : null;
		}

	}

	public List<IType> getInterfaces() {

		if (isPrimitive())
			return new ArrayList<>();

		if (isArray())
			return new ArrayList<>();

		if (!isNative()) {
			IClass clazz = Context.get().findClass(getTargetName());
			return clazz.getInterfaces();

		} else {
			List<IType> interfaces = new ArrayList<>();
			Class<?> clazz = ReflectUtils.getClass(getTargetName());
			for (Class<?> interfaceClass : clazz.getInterfaces())
				interfaces.add(TypeFactory.create(interfaceClass));
			return interfaces;
		}

	}

	public IType getWrapperType() {
		String className = ReflectUtils.getWrapperType(getClassName());
		if (className != null)
			return TypeFactory.create(className);
		return this;// 如果没有则返回自身
	}

	public boolean isMatch(IType type) {

		if (type == null)
			return false;

		// 这个方法还要判断泛型
		if (equals(type))
			return true;

		if (isMatch(type.getSuperType()))
			return true;

		for (IType inter : type.getInterfaces()) {
			if (isMatch(inter))
				return true;
		}

		return false;
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

	@Override
	public String toString() {
		throw new RuntimeException("Please use the build method of TypeBuider!className:" + getClassName());
	}

	public boolean isVoid() {
		return void.class.getName().equals(getClassName());
	}

	public boolean isObj() {
		return Object.class.getName().equals(getClassName());
	}

	public boolean isStr() {
		return String.class.getName().equals(getClassName());
	}

	public boolean isList() {
		return List.class.getName().equals(getClassName());
	}

	public boolean isMap() {
		return Map.class.getName().equals(getClassName());
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

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public void setWildcard(boolean isWildcard) {
		this.isWildcard = isWildcard;
	}

	public boolean isNative() {
		return isNative;
	}

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

}
