package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Element;

public class CtClass {

	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new LinkedHashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new LinkedHashMap<>();
	// 类名
	public String typeName;
	// 父类
	public String superName;
	// 接口
	public List<String> interfaces = new ArrayList<>();
	// 静态字段
	public List<CtField> staticFields = new ArrayList<>();
	// 静态方法
	public List<CtMethod> staticMethods = new ArrayList<>();
	// 字段
	public List<CtField> fields = new ArrayList<>();
	// 方法
	public List<CtMethod> methods = new ArrayList<>();
	// class域
	public List<Line> classLines = new ArrayList<>();

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("package --> " + packageStr + "\n");

		for (String importStr : importStrs.values()) {
			sb.append("import --> " + importStr + "\n");
		}
		for (String importStr : importAliases.values()) {
			sb.append("import alias --> " + importStr + "\n");
		}

		sb.append("typeName --> " + typeName + "\n");

		for (CtField field : staticFields) {
			sb.append("static " + field + "\n");
		}
		for (CtField field : fields) {
			sb.append(field + "\n");
		}
		for (CtMethod method : staticMethods) {
			sb.append("static " + method + "\n");
		}
		for (CtMethod method : methods) {
			sb.append(method + "\n");
		}

		return sb.toString();

	}

	public void addStaticField(CtField field) {
		checkField(field);
		staticFields.add(field);
	}

	public void addField(CtField field) {
		checkField(field);
		fields.add(field);
	}

	public void checkField(CtField field) {
		boolean flag = false;
		for (CtField f : staticFields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		for (CtField f : fields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		if (flag)
			throw new RuntimeException("Cannot have duplicate fields!number:[" + field.stmt.line.number + "], text:[ "
					+ field.stmt.line.text.trim() + " ], var:[" + field.name + "]");
	}

	/**
	 * 首先从import里面拿，如果没有，则从上下文中获取友元
	 * 
	 * @param typeName
	 * @return
	 */
	public String findClassName(String typeName) {
		// 如果本身传入的就是一个全名的话，直接返回
		if (typeName.contains("."))
			return typeName;

		// 一些基本类型，就直接返回了
		String className = getBasicType(typeName);

		// 如果传进来是个数组，那么处理一下
		boolean isArray = false;
		if (className == null) {
			if (typeName.endsWith("[]")) {
				typeName = typeName.substring(0, typeName.lastIndexOf("["));
				isArray = true;
			}
		}

		if (className == null)
			className = importStrs.get(typeName);

		// 如果不存在,则可能是在别名中
		if (className == null)
			className = importAliases.get(typeName);

		// 从上下文中获取
		if (className == null)
			className = Context.get().findImport(typeName);

		if (className == null)
			throw new RuntimeException("No import information found!name:[" + typeName + "]");

		return !isArray ? className : "[L" + className + ";";

	}

	private String getBasicType(String typeName) {
		switch (typeName) {
		case "void":
			return void.class.getName();
		case "boolean":
			return boolean.class.getName();
		case "boolean[]":
			return boolean[].class.getName();
		case "int":
			return int.class.getName();
		case "int[]":
			return int[].class.getName();
		case "long":
			return long.class.getName();
		case "long[]":
			return long[].class.getName();
		case "double":
			return double.class.getName();
		case "double[]":
			return double[].class.getName();
		case "Boolean":
			return Boolean.class.getName();
		case "Boolean[]":
			return Boolean[].class.getName();
		case "Integer":
			return Integer.class.getName();
		case "Integer[]":
			return Integer[].class.getName();
		case "Long":
			return Long.class.getName();
		case "Long[]":
			return Long[].class.getName();
		case "Double":
			return Double.class.getName();
		case "Double[]":
			return Double[].class.getName();
		case "Object":
			return Object.class.getName();
		case "Object[]":
			return Object[].class.getName();
		case "String":
			return String.class.getName();
		case "String[]":
			return String[].class.getName();
		case "List":
			return List.class.getName();
		case "Map":
			return Map.class.getName();
		default:
			return null;
		}
	}

	public boolean addImport(String className) {
		// 基本类型数组,就不必添加了
		if (className.startsWith("[") && !className.startsWith("[L"))
			return true;

		// 如果是数组
		if (className.startsWith("[L") && className.endsWith(";"))
			className = className.substring(2, className.length() - 1);

		// 如果是基本类型,就不必添加了
		if (isBasicType(className))
			return true;

		// 如果是自己本身，就不必添加了
		if ((packageStr + "." + typeName).equals(className))
			return true;

		// 如果已经存在了，就不重复添加了
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

	private boolean isBasicType(String className) {
		return void.class.getName().equals(className) || boolean.class.getName().equals(className)
				|| int.class.getName().equals(className) || long.class.getName().equals(className)
				|| double.class.getName().equals(className) || Boolean.class.getName().equals(className)
				|| Integer.class.getName().equals(className) || Long.class.getName().equals(className)
				|| Double.class.getName().equals(className) || Object.class.getName().equals(className)
				|| String.class.getName().equals(className);

	}

	public CtField findField(String fieldName) {
		for (CtField field : staticFields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		for (CtField field : fields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		throw new RuntimeException(
				"The field does not exist!class:" + packageStr + "." + typeName + ", field:" + fieldName);

	}

	public CtMethod findMethod(String methodName) {
		for (CtMethod method : staticMethods) {
			if (method.name.equals(methodName)) {
				return method;
			}
		}
		for (CtMethod method : methods) {
			if (method.name.equals(methodName)) {
				return method;
			}
		}
		throw new RuntimeException(
				"The method does not exist!class:" + packageStr + "." + typeName + ", method:" + methodName);

	}

	public List<Element> getAllElement() {
		List<Element> list = new ArrayList<>();
		list.addAll(staticFields);
		list.addAll(staticMethods);
		list.addAll(fields);
		list.addAll(methods);
		return list;
	}

}
