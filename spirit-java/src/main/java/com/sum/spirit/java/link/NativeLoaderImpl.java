package com.sum.spirit.java.link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.sum.spirit.api.link.NativeLoader;
import com.sum.spirit.java.utils.ReflectUtils;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.utils.TypeUtils;

@Component
public class NativeLoaderImpl implements NativeLoader {

	public static final Map<String, IType> TYPE_MAPPING = new ConcurrentHashMap<>();

	static {
		TYPE_MAPPING.put("VOID_WRAPPED_TYPE", IType.build("java.lang.Void", "Void", "java.lang.Void", false, false, false, false, true));
		TYPE_MAPPING.put("BOOLEAN_WRAPPED_TYPE", IType.build("java.lang.Boolean", "Boolean", "java.lang.Boolean", false, false, false, false, true));
		TYPE_MAPPING.put("CHAR_WRAPPED_TYPE", IType.build("java.lang.Character", "Character", "java.lang.Character", false, false, false, false, true));
		TYPE_MAPPING.put("BYTE_WRAPPED_TYPE", IType.build("java.lang.Byte", "Byte", "java.lang.Byte", false, false, false, false, true));
		TYPE_MAPPING.put("SHORT_WRAPPED_TYPE", IType.build("java.lang.Short", "Short", "java.lang.Short", false, false, false, false, true));
		TYPE_MAPPING.put("INT_WRAPPED_TYPE", IType.build("java.lang.Integer", "Integer", "java.lang.Integer", false, false, false, false, true));
		TYPE_MAPPING.put("LONG_WRAPPED_TYPE", IType.build("java.lang.Long", "Long", "java.lang.Long", false, false, false, false, true));
		TYPE_MAPPING.put("FLOAT_WRAPPED_TYPE", IType.build("java.lang.Float", "Float", "java.lang.Float", false, false, false, false, true));
		TYPE_MAPPING.put("DOUBLE_WRAPPED_TYPE", IType.build("java.lang.Double", "Double", "java.lang.Double", false, false, false, false, true));

		TYPE_MAPPING.put("OBJECT_TYPE", IType.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, false, true));
		TYPE_MAPPING.put("STRING_TYPE", IType.build("java.lang.String", "String", "java.lang.String", false, false, false, false, true));
		TYPE_MAPPING.put("OBJECT_ARRAY_TYPE", IType.build("[Ljava.lang.Object;", "Object[]", "java.lang.Object[]", false, true/* array */, false, false, true));
		TYPE_MAPPING.put("STRING_ARRAY_TYPE", IType.build("[Ljava.lang.String;", "String[]", "java.lang.String[]", false, true/* array */, false, false, true));

		TYPE_MAPPING.put("CLASS_TYPE", IType.build("java.lang.Class", "Class", "java.lang.Class", false, false, false, false, true));
		TYPE_MAPPING.put("LIST_TYPE", IType.build("java.util.List", "List", "java.util.List", false, false, false, false, true));
		TYPE_MAPPING.put("MAP_TYPE", IType.build("java.util.Map", "Map", "java.util.Map", false, false, false, false, true));

		TYPE_MAPPING.put("NULL_TYPE", IType.build("java.lang.Object", "Object", "java.lang.Object", false, false, true/* null */, false, true));
		TYPE_MAPPING.put("WILDCARD_TYPE", IType.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, true/* wildcard */, true));
	}

	@Override
	public IType loadType(String name) {
		return TYPE_MAPPING.get(name);
	}

	@Override
	public String findCommonType(String simpleName) {
		return ReflectUtils.getClassName(TypeUtils.getTargetName(simpleName), TypeUtils.isArray(simpleName));
	}

}
