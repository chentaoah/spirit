package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Annotated;
import com.sum.shy.core.api.Element;
import com.sum.shy.core.utils.ReflectUtils;

public class CtClass implements Annotated {
	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new LinkedHashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new LinkedHashMap<>();
	// 类上的注解
	public List<String> annotations = new ArrayList<>();
	// 类别
	public String category;
	// 类名
	public String typeName;
	// 父类
	public String superName;
	// 接口(接口继承的接口也在这个里面)
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
	 * 是否已经引入
	 * 
	 * @param typeName
	 * @return
	 */
	public boolean existImport(String typeName) {
		return importStrs.containsKey(typeName) || importAliases.containsKey(typeName);
	}

	/**
	 * 不再从友元里面找，因为友元已经在前面自动引入了
	 * 
	 * @param simpleName
	 * @return
	 */
	public String getClassName(String simpleName) {
		// 如果本身传入的就是一个全名的话，直接返回
		if (simpleName.contains("."))
			return simpleName;

		// 如果传进来是个数组，那么处理一下
		boolean isArray = ReflectUtils.isArray(simpleName);
		String typeName = ReflectUtils.getTypeName(simpleName);
		// 1.首先先去引入里面找
		String className = null;
		if (className == null)
			className = importStrs.get(typeName);
		if (className == null)
			className = importAliases.get(typeName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 2.可能是这个类本身
		if (this.typeName.equals(typeName))
			return packageStr + "." + this.typeName;

		// 3.如果没有引入的话，可能是一些基本类型java.lang包下的
		if (className == null)
			className = ReflectUtils.getCommonType(simpleName);
		if (className != null)
			return className;

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");

	}

	public boolean addImport(String className) {
		// 基本类型数组,就不必添加了
		if (className.startsWith("[") && !className.startsWith("[L"))
			return true;

		// 如果是数组
		if (className.startsWith("[L") && className.endsWith(";"))
			className = className.substring(2, className.length() - 1);

		// 1.基本类className和simpleName相同
		// 2.一般java.lang.包下的类不用引入
		if (ReflectUtils.getCommonType(className) != null || className.startsWith("java.lang."))
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

	public boolean existField(String fieldName) {
		for (CtField field : staticFields) {
			if (field.name.equals(fieldName)) {
				return true;
			}
		}
		for (CtField field : fields) {
			if (field.name.equals(fieldName)) {
				return true;
			}
		}
		return false;
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

	public boolean existMethod(String methodName) {
		for (CtMethod method : staticMethods) {
			if (method.name.equals(methodName)) {
				return true;
			}
		}
		for (CtMethod method : methods) {
			if (method.name.equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	public List<Element> getAllElement() {
		List<Element> list = new ArrayList<>();
		list.addAll(staticFields);
		list.addAll(staticMethods);
		list.addAll(fields);
		list.addAll(methods);
		return list;
	}

	@Override
	public List<String> getAnnotations() {
		return annotations;
	}

}
