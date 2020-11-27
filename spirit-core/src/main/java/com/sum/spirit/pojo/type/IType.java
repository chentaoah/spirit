package com.sum.spirit.pojo.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.sum.spirit.core.type.TypeFactory;
import com.sum.spirit.pojo.enums.ModifierEnum;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

public class IType {

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

	public boolean isGenericType() {
		return genericTypes != null && genericTypes.size() > 0;
	}

	public boolean isTypeVariable() {
		return StringUtils.isNotEmpty(genericName);
	}

	public IType getWrappedType() {
		IType wrappedType = TypeEnum.getWrappedType(getClassName());
		return wrappedType != null ? wrappedType : this;
	}

	public String getTargetName() {// 返回真正的className,包括数组中的
		return TypeUtils.getTargetName(getClassName());
	}

	public IType getTargetType() {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		return factory.create(getTargetName());
	}

	public IType toSuper() {
		this.setModifiers(ModifierEnum.SUPER.value);
		return this;
	}

	public IType toThis() {
		this.setModifiers(ModifierEnum.THIS.value);
		return this;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof IType)) {
			return false;
		}

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
		if (isGenericType()) {
			return className + "<" + Joiner.on(", ").join(genericTypes) + ">";
		}
		if (isTypeVariable()) {
			return genericName;
		}
		return className;
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
