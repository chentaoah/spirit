package com.sum.spirit.java.link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.sum.spirit.api.link.TypeAdapter;
import com.sum.spirit.pojo.clazz.IType;

@Component
public class TypeAdapterImpl implements TypeAdapter {

	public static final Map<String, IType> TYPE_MAPPING = new ConcurrentHashMap<>();

	public static final IType NULL_TYPE;
	public static final IType WILDCARD_TYPE;

	static {
		TYPE_MAPPING.put("spirit.lang.Void", build("java.lang.Void", "Void", "java.lang.Void", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Boolean", build("java.lang.Boolean", "Boolean", "java.lang.Boolean", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Character", build("java.lang.Character", "Character", "java.lang.Character", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Byte", build("java.lang.Byte", "Byte", "java.lang.Byte", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Short", build("java.lang.Short", "Short", "java.lang.Short", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Integer", build("java.lang.Integer", "Integer", "java.lang.Integer", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Long", build("java.lang.Long", "Long", "java.lang.Long", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Float", build("java.lang.Float", "Float", "java.lang.Float", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.Double", build("java.lang.Double", "Double", "java.lang.Double", false, false, false, false));

		TYPE_MAPPING.put("spirit.lang.Object", build("java.lang.Object", "Object", "java.lang.Object", false, false, false, false));
		TYPE_MAPPING.put("spirit.lang.String", build("java.lang.String", "String", "java.lang.String", false, false, false, false));
		TYPE_MAPPING.put("[Lspirit.lang.Object;", build("[Ljava.lang.Object;", "Object[]", "java.lang.Object[]", false, true/* array */, false, false));
		TYPE_MAPPING.put("[Lspirit.lang.String;", build("[Ljava.lang.String;", "String[]", "java.lang.String[]", false, true/* array */, false, false));

		NULL_TYPE = build("java.lang.Object", "Object", "java.lang.Object", false, false, true/* null */, false);
		WILDCARD_TYPE = build("java.lang.Object", "Object", "java.lang.Object", false, false, false, true/* wildcard */);
	}

	public static IType build(String className, String simpleName, String typeName, boolean isPrimitive, boolean isArray, boolean isNull, boolean isWildcard) {
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(simpleName);
		type.setTypeName(typeName);
		type.setGenericName(null);
		type.setPrimitive(isPrimitive);
		type.setArray(isArray);
		type.setNull(isNull);
		type.setWildcard(isWildcard);
		type.setNative(true);
		type.setModifiers(IType.PUBLIC_MODIFIERS);
		type.setGenericTypes(null);
		return type;
	}

	@Override
	public IType adapte(IType type) {
		if (type.isNull()) {
			return NULL_TYPE;
		} else if (type.isWildcard()) {
			return WILDCARD_TYPE;
		} else if (TYPE_MAPPING.containsKey(type.getClassName())) {
			return TYPE_MAPPING.get(type.getClassName());
		}
		return type;
	}

}
