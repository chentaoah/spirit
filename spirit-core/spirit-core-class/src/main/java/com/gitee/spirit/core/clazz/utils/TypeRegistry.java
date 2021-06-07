package com.gitee.spirit.core.clazz.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gitee.spirit.common.enums.PrimitiveEnum;
import com.gitee.spirit.common.utils.ConfigUtils;
import com.gitee.spirit.core.clazz.entity.IType;

public class TypeRegistry {

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
	public static final IType CLASS;
	public static final IType LIST;
	public static final IType MAP;
	public static final IType OBJECT_ARRAY;
	public static final IType STRING_ARRAY;
	public static final IType NULL;
	public static final IType WILDCARD;

	public static final Map<String, IType> BOX_TYPE_MAPPING = new ConcurrentHashMap<>();

	static {
		VOID = TypeBuilder.build(PrimitiveEnum.VOID);
		BOOLEAN = TypeBuilder.build(PrimitiveEnum.BOOLEAN);
		CHAR = TypeBuilder.build(PrimitiveEnum.CHAR);
		BYTE = TypeBuilder.build(PrimitiveEnum.BYTE);
		SHORT = TypeBuilder.build(PrimitiveEnum.SHORT);
		INT = TypeBuilder.build(PrimitiveEnum.INT);
		LONG = TypeBuilder.build(PrimitiveEnum.LONG);
		FLOAT = TypeBuilder.build(PrimitiveEnum.FLOAT);
		DOUBLE = TypeBuilder.build(PrimitiveEnum.DOUBLE);

		BOOLEAN_ARRAY = TypeBuilder.build(PrimitiveEnum.BOOLEAN_ARRAY);
		CHAR_ARRAY = TypeBuilder.build(PrimitiveEnum.CHAR_ARRAY);
		BYTE_ARRAY = TypeBuilder.build(PrimitiveEnum.BYTE_ARRAY);
		SHORT_ARRAY = TypeBuilder.build(PrimitiveEnum.SHORT_ARRAY);
		INT_ARRAY = TypeBuilder.build(PrimitiveEnum.INT_ARRAY);
		LONG_ARRAY = TypeBuilder.build(PrimitiveEnum.INT_ARRAY);
		FLOAT_ARRAY = TypeBuilder.build(PrimitiveEnum.FLOAT_ARRAY);
		DOUBLE_ARRAY = TypeBuilder.build(PrimitiveEnum.DOUBLE_ARRAY);

		String langPackage = ConfigUtils.getLangPackage() + ".";
		String utilPackage = ConfigUtils.getUtilPackage() + ".";
		VOID_BOX = TypeBuilder.build(langPackage + "Void", "Void", langPackage + "Void", false, false, false, false, true);
		BOOLEAN_BOX = TypeBuilder.build(langPackage + "Boolean", "Boolean", langPackage + "Boolean", false, false, false, false, true);
		CHAR_BOX = TypeBuilder.build(langPackage + "Character", "Character", langPackage + "Character", false, false, false, false, true);
		BYTE_BOX = TypeBuilder.build(langPackage + "Byte", "Byte", langPackage + "Byte", false, false, false, false, true);
		SHORT_BOX = TypeBuilder.build(langPackage + "Short", "Short", langPackage + "Short", false, false, false, false, true);
		INT_BOX = TypeBuilder.build(langPackage + "Integer", "Integer", langPackage + "Integer", false, false, false, false, true);
		LONG_BOX = TypeBuilder.build(langPackage + "Long", "Long", langPackage + "Long", false, false, false, false, true);
		FLOAT_BOX = TypeBuilder.build(langPackage + "Float", "Float", langPackage + "Float", false, false, false, false, true);
		DOUBLE_BOX = TypeBuilder.build(langPackage + "Double", "Double", langPackage + "Double", false, false, false, false, true);
		OBJECT = TypeBuilder.build(langPackage + "Object", "Object", langPackage + "Object", false, false, false, false, true);
		STRING = TypeBuilder.build(langPackage + "String", "String", langPackage + "String", false, false, false, false, true);
		CLASS = TypeBuilder.build(langPackage + "Class", "Class", langPackage + "Class", false, false, false, false, true);
		LIST = TypeBuilder.build(utilPackage + "List", "List", utilPackage + "List", false, false, false, false, true);
		MAP = TypeBuilder.build(utilPackage + "Map", "Map", utilPackage + "Map", false, false, false, false, true);

		OBJECT_ARRAY = TypeBuilder.build("[L" + langPackage + "Object;", "Object[]", langPackage + "Object[]", false, true/* array */, false, false, true);
		STRING_ARRAY = TypeBuilder.build("[L" + langPackage + "String;", "String[]", langPackage + "String[]", false, true/* array */, false, false, true);
		NULL = TypeBuilder.build(langPackage + "Object", "Object", langPackage + "Object", false, false, true/* null */, false, true);
		WILDCARD = TypeBuilder.build(langPackage + "Object", "Object", langPackage + "Object", false, false, false, true/* wildcard */, true);

		BOX_TYPE_MAPPING.put(VOID.getClassName(), VOID_BOX);
		BOX_TYPE_MAPPING.put(BOOLEAN.getClassName(), BOOLEAN_BOX);
		BOX_TYPE_MAPPING.put(CHAR.getClassName(), CHAR_BOX);
		BOX_TYPE_MAPPING.put(BYTE.getClassName(), BYTE_BOX);
		BOX_TYPE_MAPPING.put(SHORT.getClassName(), SHORT_BOX);
		BOX_TYPE_MAPPING.put(INT.getClassName(), INT_BOX);
		BOX_TYPE_MAPPING.put(LONG.getClassName(), LONG_BOX);
		BOX_TYPE_MAPPING.put(FLOAT.getClassName(), FLOAT_BOX);
		BOX_TYPE_MAPPING.put(DOUBLE.getClassName(), DOUBLE_BOX);
	}

	public static IType getBoxType(String className) {
		return BOX_TYPE_MAPPING.get(className);
	}

}
