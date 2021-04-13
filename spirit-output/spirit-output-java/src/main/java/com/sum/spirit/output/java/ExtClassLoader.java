package com.sum.spirit.output.java;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.api.StaticTypesCtor;
import com.sum.spirit.core.clazz.AbstractClassLoader;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.TypeBuilder;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.output.java.utils.ReflectUtils;

@Component
@Order(-80)
public class ExtClassLoader extends AbstractClassLoader<Class<?>> implements StaticTypesCtor {

	@Override
	public Map<String, IType> prepareStaticTypes() {
		Map<String, IType> typeMap = new HashMap<>();
		typeMap.put("VOID_BOX", TypeBuilder.build("java.lang.Void", "Void", "java.lang.Void", false, false, false, false, true));
		typeMap.put("BOOLEAN_BOX", TypeBuilder.build("java.lang.Boolean", "Boolean", "java.lang.Boolean", false, false, false, false, true));
		typeMap.put("CHAR_BOX", TypeBuilder.build("java.lang.Character", "Character", "java.lang.Character", false, false, false, false, true));
		typeMap.put("BYTE_BOX", TypeBuilder.build("java.lang.Byte", "Byte", "java.lang.Byte", false, false, false, false, true));
		typeMap.put("SHORT_BOX", TypeBuilder.build("java.lang.Short", "Short", "java.lang.Short", false, false, false, false, true));
		typeMap.put("INT_BOX", TypeBuilder.build("java.lang.Integer", "Integer", "java.lang.Integer", false, false, false, false, true));
		typeMap.put("LONG_BOX", TypeBuilder.build("java.lang.Long", "Long", "java.lang.Long", false, false, false, false, true));
		typeMap.put("FLOAT_BOX", TypeBuilder.build("java.lang.Float", "Float", "java.lang.Float", false, false, false, false, true));
		typeMap.put("DOUBLE_BOX", TypeBuilder.build("java.lang.Double", "Double", "java.lang.Double", false, false, false, false, true));

		typeMap.put("OBJECT", TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, false, true));
		typeMap.put("STRING", TypeBuilder.build("java.lang.String", "String", "java.lang.String", false, false, false, false, true));
		typeMap.put("OBJECT_ARRAY", TypeBuilder.build("[Ljava.lang.Object;", "Object[]", "java.lang.Object[]", false, true/* array */, false, false, true));
		typeMap.put("STRING_ARRAY", TypeBuilder.build("[Ljava.lang.String;", "String[]", "java.lang.String[]", false, true/* array */, false, false, true));
		typeMap.put("CLASS", TypeBuilder.build("java.lang.Class", "Class", "java.lang.Class", false, false, false, false, true));
		typeMap.put("LIST", TypeBuilder.build("java.util.List", "List", "java.util.List", false, false, false, false, true));
		typeMap.put("MAP", TypeBuilder.build("java.util.Map", "Map", "java.util.Map", false, false, false, false, true));
		typeMap.put("NULL", TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, true/* null */, false, true));
		typeMap.put("WILDCARD", TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, true/* wildcard */, true));
		return typeMap;
	}

	@Override
	public List<URL> getResources() {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public List<String> getNames() {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public boolean contains(String name) {
		return name.startsWith("java.lang.");
	}

	@Override
	public Class<?> findClass(String name) {
		return ReflectUtils.getClass(name);
	}

	@Override
	public Class<?> findLoadedClass(String name) {
		return ReflectUtils.getClass(name);
	}

	@Override
	public List<Class<?>> getAllClasses() {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public URL findResource(String name) {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public Class<?> defineClass(String name, URL resource) {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public String findClassName(String simpleName) {
		return ReflectUtils.getClassName(TypeUtils.getTargetName(simpleName), TypeUtils.isArray(simpleName));
	}

	@Override
	public boolean shouldImport(String selfClassName, String className) {
		return false;
	}

}
