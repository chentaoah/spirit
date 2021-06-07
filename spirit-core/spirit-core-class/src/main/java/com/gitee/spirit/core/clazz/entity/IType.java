package com.gitee.spirit.core.clazz.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gitee.spirit.common.enums.ModifierEnum;
import com.gitee.spirit.common.utils.SpringUtils;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.utils.TypeTable;
import com.gitee.spirit.core.clazz.utils.TypeUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IType {

	private String className;
	private String simpleName;
	private String typeName;
	private String genericName;// T K
	private boolean isPrimitive;// 是否原始类型
	private boolean isArray;// 是否数组
	private boolean isNull;// 是否空值
	private boolean isWildcard;// 是否“?”
	private boolean isNative;// 是否本地类型
	private int modifiers;// 进行位运算后得到的修饰符

	@NonNull
	@Builder.Default
	private List<IType> genericTypes = new ArrayList<>();// 泛型参数

	public boolean isTypeVariable() {
		return StringUtils.isNotEmpty(genericName);
	}

	public boolean isGenericType() {
		return genericTypes != null && genericTypes.size() > 0;
	}

	public IType toBox() {
		IType boxType = TypeTable.getBoxType(getClassName());
		return boxType != null ? boxType : this;
	}

	public String getTargetName() {// 返回真正的className,包括数组中的
		return TypeUtils.getTargetName(getClassName());
	}

	public IType toTarget() {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		return factory.create(getTargetName());
	}

	public IType withSuperModifiers() {
		this.setModifiers(ModifierEnum.SUPER.value);
		return this;
	}

	public IType withThisModifiers() {
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
	public String toString() {// 只打印当前类型的信息，不包括泛型
		if (isWildcard()) {
			return "?";
		}
		if (isTypeVariable()) {
			return getGenericName();
		}
		return getSimpleName();
	}

}
