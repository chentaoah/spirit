package com.sum.spirit.java;

import java.net.URL;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.TypeEnumCtor;
import com.sum.spirit.core.AbstractClassLoader;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.TypeBuilder;

@Component
@Order(-80)
public class ExtClassLoader extends AbstractClassLoader<Class<?>> implements TypeEnumCtor {

	@Override
	public void prepareEnv() {
		TypeEnum.Void.value = TypeBuilder.build("java.lang.Void", "Void", "java.lang.Void", false, false, false, false, true);
		TypeEnum.Boolean.value = TypeBuilder.build("java.lang.Boolean", "Boolean", "java.lang.Boolean", false, false, false, false, true);
		TypeEnum.Character.value = TypeBuilder.build("java.lang.Character", "Character", "java.lang.Character", false, false, false, false, true);
		TypeEnum.Byte.value = TypeBuilder.build("java.lang.Byte", "Byte", "java.lang.Byte", false, false, false, false, true);
		TypeEnum.Short.value = TypeBuilder.build("java.lang.Short", "Short", "java.lang.Short", false, false, false, false, true);
		TypeEnum.Integer.value = TypeBuilder.build("java.lang.Integer", "Integer", "java.lang.Integer", false, false, false, false, true);
		TypeEnum.Long.value = TypeBuilder.build("java.lang.Long", "Long", "java.lang.Long", false, false, false, false, true);
		TypeEnum.Float.value = TypeBuilder.build("java.lang.Float", "Float", "java.lang.Float", false, false, false, false, true);
		TypeEnum.Double.value = TypeBuilder.build("java.lang.Double", "Double", "java.lang.Double", false, false, false, false, true);

		TypeEnum.Object.value = TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, false, true);
		TypeEnum.String.value = TypeBuilder.build("java.lang.String", "String", "java.lang.String", false, false, false, false, true);
		TypeEnum.Object_Array.value = TypeBuilder.build("[Ljava.lang.Object;", "Object[]", "java.lang.Object[]", false, true/* array */, false, false, true);
		TypeEnum.String_Array.value = TypeBuilder.build("[Ljava.lang.String;", "String[]", "java.lang.String[]", false, true/* array */, false, false, true);

		TypeEnum.Class.value = TypeBuilder.build("java.lang.Class", "Class", "java.lang.Class", false, false, false, false, true);
		TypeEnum.List.value = TypeBuilder.build("java.util.List", "List", "java.util.List", false, false, false, false, true);
		TypeEnum.Map.value = TypeBuilder.build("java.util.Map", "Map", "java.util.Map", false, false, false, false, true);

		TypeEnum.Null.value = TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, true/* null */, false, true);
		TypeEnum.Wildcard.value = TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, true/* wildcard */, true);
	}

	@Override
	public List<URL> getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<?> findClass(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> findLoadedClass(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Class<?>> getAllClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL findResource(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> defineClass(String name, URL resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findClassName(String simpleName) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public String findClassName(String simpleName) {
//		return ReflectUtils.getClassName(TypeUtils.getTargetName(simpleName), TypeUtils.isArray(simpleName));
//	}
//
//	@Override
//	public boolean contains(String className) {
//		return className.startsWith("java.lang.");
//	}
//
//	@Override
//	public boolean shouldImport(String selfClassName, String className) {
//		return false;
//	}
//
//	@Override
//	@SuppressWarnings("unchecked")
//	public <T> T getClass(String className) {
//		return (T) ReflectUtils.getClass(className);// 可能是数组
//	}

}
