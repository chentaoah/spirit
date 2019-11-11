package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.utils.ReflectUtils;

public class Clazz {

	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new HashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new HashMap<>();
	// 预处理
	public Map<String, String> defTypes = new HashMap<>();
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
		if (importStr == null) {
			throw new RuntimeException("No import information found!type:[" + type + "]");
		}
		return importStr;

	}

	public void addImport(NativeType nativeType) {
		if (nativeType == null)
			return;
		nativeType.forceFullName = !addImport(nativeType.getName());
		for (NativeType subNativeType : nativeType.genericTypes.values()) {
			addImport(subNativeType);
		}
	}

	public boolean addImport(String className) {
		// 如果是基本类型,就不必添加了
		if (ReflectUtils.isPrimitive(className)) {
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

}
