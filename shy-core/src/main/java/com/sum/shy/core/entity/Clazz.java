package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.utils.ReflectUtils;

public class Clazz {

	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new LinkedHashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new LinkedHashMap<>();
	// 类名
	public String className;
	// 父类
	public String superName;
	// 接口
	public List<String> interfaces = new ArrayList<>();
	// 静态字段
	public List<Field> staticFields = new ArrayList<>();
	// 静态方法
	public List<Method> staticMethods = new ArrayList<>();
	// 字段
	public List<Field> fields = new ArrayList<>();
	// 方法
	public List<Method> methods = new ArrayList<>();
	// class域
	public List<Line> classLines;

	public void show() {
		System.out.println("=============================");

		System.out.println("package --> " + packageStr);

		for (String importStr : importStrs.values()) {
			System.out.println("import --> " + importStr);
		}
		for (String importStr : importAliases.values()) {
			System.out.println("import alias --> " + importStr);
		}

		System.out.println("className --> " + className);

		for (Field field : staticFields) {
			System.out.println("static field --> " + field);
		}
		for (Field field : fields) {
			System.out.println("field --> " + field);
		}
		for (Method method : staticMethods) {
			System.out.println("static method --> " + method);
		}
		for (Method method : methods) {
			System.out.println("method --> " + method);
		}

		System.out.println("=============================");

	}

	public void addStaticField(Field field) {
		checkField(field);
		staticFields.add(field);
	}

	public void addField(Field field) {
		checkField(field);
		fields.add(field);
	}

	public void checkField(Field field) {
		boolean flag = false;
		for (Field f : staticFields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		for (Field f : fields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		if (flag)
			throw new RuntimeException("Cannot have duplicate fields!number:[" + field.stmt.line.number + "], text:[ "
					+ field.stmt.line.text.trim() + " ], var:[" + field.name + "]");
	}

	public String findImport(String type) {
		// 如果本身传入的就是一个全名的话，直接返回
		if (type.contains(".")) {
			return type;
		}

		String importStr = importStrs.get(type);
		// 如果不存在,则可能是在别名中
		if (importStr == null) {
			importStr = importAliases.get(type);
		}

		// 从上下文中获取
		importStr = Context.get().findImport(type);

		if (importStr == null) {
			throw new RuntimeException("No import information found!name:[" + type + "]");
		}

		return importStr;

	}

	public boolean isAlias(String type) {
		return importAliases.containsKey(type);
	}

	public void addImport(Type type) {
//		if (name == null)
//			return;
//		name.forceFullName = !addImport(name.getName());
//		for (name subNativeType : name.genericTypes.values()) {
//			addImport(subNativeType);
//		}
	}

	public boolean addImport(String className) {
		// 如果是数组
		if (className.startsWith("[L") && className.endsWith(";")) {
			className = className.substring(2, className.length() - 1);
		}
		// 如果是基本类型,就不必添加了
		if (ReflectUtils.isPrimitiveType(className)) {
			return true;
		}
		if (!importStrs.containsValue(className)) {
			String lastName = className.substring(className.lastIndexOf(".") + 1);
			// 如果不存在该类,但是出现了类名重复的,则返回false,表示添加失败
			if (importStrs.containsKey(lastName)) {
				return false;
			}
			importStrs.put(lastName, className);
			return true;
		}
		// 重复添加也认为是添加成功了
		return true;
	}

	public Method findMethod(String methodName) {
		for (Method method : staticMethods) {
			if (method.name.equals(methodName)) {
				return method;
			}
		}
		for (Method method : methods) {
			if (method.name.equals(methodName)) {
				return method;
			}
		}
		throw new RuntimeException("No method information found!methodName:[" + methodName + "]");

	}

}
