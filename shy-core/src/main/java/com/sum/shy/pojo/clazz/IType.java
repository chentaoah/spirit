package com.sum.shy.pojo.clazz;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.api.link.ClassLinker;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.pojo.common.StaticType;
import com.sum.shy.utils.ReflectUtils;
import com.sum.shy.utils.TypeUtils;

/**
 * 指的是已经在实际代码中，使用的类型描述
 * 
 * @author chentao26275
 *
 */
public class IType {

	public static final int THIS_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

	public static final int SUPER_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED;

	public static final int PUBLIC_MODIFIERS = Modifier.PUBLIC;

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	public static ClassLinker linker = ProxyFactory.get(ClassLinker.class);

	private String className;
	private String simpleName;
	private String typeName;
	private String genericName;// T K
	private boolean isPrimitive;// 是否基础类型
	private boolean isArray;// 是否数组
	private boolean isNull;// 是否空值
	private boolean isWildcard;// 是否 ？
	private boolean isNative;// 是否本地类型
	private int modifiers;// 进行位运算后得到的修饰符
	private List<IType> genericTypes = new ArrayList<>();// 泛型参数

	public IType copy() {
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(simpleName);
		type.setTypeName(typeName);
		type.setGenericName(genericName);
		type.setPrimitive(isPrimitive);
		type.setArray(isArray);
		type.setNull(isNull);
		type.setWildcard(isWildcard);
		type.setNative(isNative);
		type.setModifiers(modifiers);
		type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return type;
	}

	public String getTargetName() {// 返回真正的className,包括数组中的
		return TypeUtils.getTargetName(getClassName());
	}

	public IType getTargetType() {
		return factory.create(getTargetName());
	}

	public IType getSuperType() {

		if (isPrimitive())
			return null;

		if (isArray())
			return StaticType.OBJECT_TYPE;

		IType superType = linker.getSuperType(this);

		if (superType == null)
			return null;

		if (modifiers == IType.THIS_MODIFIERS || modifiers == IType.SUPER_MODIFIERS) {
			superType.setModifiers(IType.SUPER_MODIFIERS);

		} else if (modifiers == IType.PUBLIC_MODIFIERS) {
			superType.setModifiers(IType.PUBLIC_MODIFIERS);
		}

		return superType;
	}

	public List<IType> getInterfaceTypes() {

		if (isPrimitive())
			return new ArrayList<>();

		if (isArray())
			return new ArrayList<>();

		return linker.getInterfaceTypes(this);
	}

	public IType getWrappedType() {
		Class<?> clazz = ReflectUtils.getWrappedType(getClassName());
		if (clazz != null)
			return factory.create(clazz);
		return this;// 如果没有则返回自身
	}

	public boolean isMatch(IType type) {

		if (type == null)
			return false;

		// Null can not match any type
		if (isNull())
			return false;

		// Any type can match null
		if (type.isNull())
			return true;

		// 这个方法还要判断泛型
		if (equals(type))
			return true;

		// 这个方法中，还要考虑到自动拆组包
		if (isMatch(type.getWrappedType().getSuperType()))
			return true;

		for (IType inter : type.getInterfaceTypes()) {
			if (isMatch(inter))
				return true;
		}

		return false;
	}

	public boolean isGenericType() {
		return genericTypes != null && genericTypes.size() > 0;
	}

	public boolean isTypeVariable() {
		return StringUtils.isNotEmpty(genericName);
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof IType))
			return false;

		IType typeToMatch = (IType) obj;
		boolean flag = getClassName().equals(typeToMatch.getClassName());
		if (flag) {
			int count = 0;
			for (IType genericType : getGenericTypes()) {
				if (!genericType.equals(typeToMatch.getGenericTypes().get(count++))) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public String toString() {
		if (isGenericType())
			return className + "<" + Joiner.on(", ").join(genericTypes) + ">";
		if (isTypeVariable())
			return genericName;
		return className;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
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

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public List<IType> getGenericTypes() {
		return genericTypes;
	}

	public void setGenericTypes(List<IType> genericTypes) {
		this.genericTypes = genericTypes != null ? genericTypes : new ArrayList<>();
	}

}
