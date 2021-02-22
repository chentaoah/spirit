package com.sum.spirit.core.clazz.constants;

import java.util.Map;

import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.common.enums.PrimitiveEnum;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.api.StaticTypesCtor;
import com.sum.spirit.core.clazz.entity.IType;

import cn.hutool.core.lang.Assert;

public class StaticTypes {

	public static final IType VOID;
	public static final IType BOOLEAN;
	public static final IType CHAR;
	public static final IType BYTE;
	public static final IType SHORT;
	public static final IType INT;
	public static final IType LONG;
	public static final IType FLOAT;
	public static final IType DOUBLE;

	public static final IType BOOLEAN_ARRAY;
	public static final IType CHAR_ARRAY;
	public static final IType BYTE_ARRAY;
	public static final IType SHORT_ARRAY;
	public static final IType INT_ARRAY;
	public static final IType LONG_ARRAY;
	public static final IType FLOAT_ARRAY;
	public static final IType DOUBLE_ARRAY;

	public static final IType VOID_BOX;
	public static final IType BOOLEAN_BOX;
	public static final IType CHAR_BOX;
	public static final IType BYTE_BOX;
	public static final IType SHORT_BOX;
	public static final IType INT_BOX;
	public static final IType LONG_BOX;
	public static final IType FLOAT_BOX;
	public static final IType DOUBLE_BOX;

	public static final IType OBJECT;
	public static final IType STRING;
	public static final IType OBJECT_ARRAY;
	public static final IType STRING_ARRAY;
	public static final IType CLASS;
	public static final IType LIST;
	public static final IType MAP;
	public static final IType NULL;
	public static final IType WILDCARD;

	static {
		VOID = createTypeByPrimitiveEnum(PrimitiveEnum.VOID);
		BOOLEAN = createTypeByPrimitiveEnum(PrimitiveEnum.BOOLEAN);
		CHAR = createTypeByPrimitiveEnum(PrimitiveEnum.CHAR);
		BYTE = createTypeByPrimitiveEnum(PrimitiveEnum.BYTE);
		SHORT = createTypeByPrimitiveEnum(PrimitiveEnum.SHORT);
		INT = createTypeByPrimitiveEnum(PrimitiveEnum.INT);
		LONG = createTypeByPrimitiveEnum(PrimitiveEnum.LONG);
		FLOAT = createTypeByPrimitiveEnum(PrimitiveEnum.FLOAT);
		DOUBLE = createTypeByPrimitiveEnum(PrimitiveEnum.DOUBLE);

		BOOLEAN_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.BOOLEAN_ARRAY);
		CHAR_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.CHAR_ARRAY);
		BYTE_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.BYTE_ARRAY);
		SHORT_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.SHORT_ARRAY);
		INT_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.INT_ARRAY);
		LONG_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.INT_ARRAY);
		FLOAT_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.FLOAT_ARRAY);
		DOUBLE_ARRAY = createTypeByPrimitiveEnum(PrimitiveEnum.DOUBLE_ARRAY);

		StaticTypesCtor ctor = SpringUtils.getBean(StaticTypesCtor.class);
		Assert.notNull(ctor, "Static types ctor must be provided!");
		Map<String, IType> typeMap = ctor.prepareStaticTypes();

		VOID_BOX = typeMap.get("VOID_BOX");
		BOOLEAN_BOX = typeMap.get("BOOLEAN_BOX");
		CHAR_BOX = typeMap.get("CHAR_BOX");
		BYTE_BOX = typeMap.get("BYTE_BOX");
		SHORT_BOX = typeMap.get("SHORT_BOX");
		INT_BOX = typeMap.get("INT_BOX");
		LONG_BOX = typeMap.get("LONG_BOX");
		FLOAT_BOX = typeMap.get("FLOAT_BOX");
		DOUBLE_BOX = typeMap.get("DOUBLE_BOX");

		OBJECT = typeMap.get("OBJECT");
		STRING = typeMap.get("STRING");
		OBJECT_ARRAY = typeMap.get("OBJECT_ARRAY");
		STRING_ARRAY = typeMap.get("STRING_ARRAY");
		CLASS = typeMap.get("CLASS");
		LIST = typeMap.get("LIST");
		MAP = typeMap.get("MAP");
		NULL = typeMap.get("NULL");
		WILDCARD = typeMap.get("WILDCARD");
	}

	public static IType createTypeByPrimitiveEnum(PrimitiveEnum primitiveEnum) {
		return IType.builder().className(primitiveEnum.className).simpleName(primitiveEnum.simpleName).typeName(primitiveEnum.typeName)
				.isPrimitive(primitiveEnum.isPrimitive).isArray(primitiveEnum.isArray).modifiers(ModifierEnum.PUBLIC.value).build();
	}

	public static IType getWrappedType(String className) {
		if (VOID.getClassName().equals(className)) {
			return VOID_BOX;

		} else if (BOOLEAN.getClassName().equals(className)) {
			return BOOLEAN_BOX;

		} else if (CHAR.getClassName().equals(className)) {
			return CHAR_BOX;

		} else if (BYTE.getClassName().equals(className)) {
			return BYTE_BOX;

		} else if (SHORT.getClassName().equals(className)) {
			return SHORT_BOX;

		} else if (INT.getClassName().equals(className)) {
			return INT_BOX;

		} else if (LONG.getClassName().equals(className)) {
			return LONG_BOX;

		} else if (FLOAT.getClassName().equals(className)) {
			return FLOAT_BOX;

		} else if (DOUBLE.getClassName().equals(className)) {
			return DOUBLE_BOX;
		}
		return null;
	}

}
