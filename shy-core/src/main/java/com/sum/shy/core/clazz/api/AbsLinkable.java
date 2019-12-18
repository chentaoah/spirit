package com.sum.shy.core.clazz.api;

import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.core.entity.Context;
import com.sum.shy.core.utils.ReflectUtils;

public abstract class AbsLinkable extends AbsDescribable implements Linkable {
	// 包名
	public String packageStr;
	// 引入
	public Map<String, String> importStrs = new LinkedHashMap<>();
	// 别名引入
	public Map<String, String> importAliases = new LinkedHashMap<>();

	public String getPackage() {
		return packageStr;
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
	 * 在字段声明语句中，这个时候还没有自动引入友元，导致报错
	 * 
	 * @param simpleName
	 * @return
	 */
	public String findImport(String simpleName) {

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

		// 2.友元,注意这个类本身也在所有类之中
		if (className == null)
			className = Context.get().findFriend(typeName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 3.内部类
//		if (innerClasses.containsKey(typeName))
//			return innerClasses.get(typeName).getClassName();

		// 4.如果没有引入的话，可能是一些基本类型java.lang包下的
		if (className == null)
			className = ReflectUtils.getCommonType(simpleName);
		if (className == null)
			className = ReflectUtils.getCollectionType(typeName);
		if (className != null)
			return className;

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");

	}

	public boolean addImport(String className) {

		// 1.基本类型数组,不添加
		if (className.startsWith("[") && !className.startsWith("[L"))
			return true;

		// 如果是数组，则把修饰符号去掉
		className = ReflectUtils.getClassName(className);

		// 2.基本类className和simpleName相同
		// 3.一般java.lang.包下的类不用引入
		if (ReflectUtils.getCommonType(className) != null || className.startsWith("java.lang."))
			return true;

		// 4.别名,不添加
		if (importAliases.containsValue(className))
			return true;

		// 5.如果是本身,不添加
//		if (getClassName().equals(className))
//			return true;
//
//		// 6.内部类不添加
//		for (CtClass innerClass : innerClasses.values()) {
//			if (innerClass.getClassName().equals(className))
//				return true;
//		}

		// 7.重复的不添加
		if (!importStrs.containsValue(className)) {
			String lastName = className.substring(className.lastIndexOf(".") + 1);
			// 如果不存在该类,但是出现了类名重复的,则返回false,表示添加失败
			if (importStrs.containsKey(lastName))
				return false;
			importStrs.put(lastName, className);
			return true;
		}
		// 重复添加也认为是添加成功了
		return true;
	}

}
