package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.pojo.common.Context;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.utils.TypeUtils;

public abstract class Importable {

	public List<Import> imports = new ArrayList<>();

	public String findImport(String simpleName) {
		// 如果是className，则直接返回
		if (simpleName.contains("."))
			return simpleName;

		// 如果传进来是个数组，那么处理一下
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);

		// 1.首先先去引入里面找
		for (Import imp : imports) {
			if (imp.isMatch(targetName))
				return !isArray ? imp.getClassName() : "[L" + imp.getClassName() + ";";
		}

		// 2.在所有类里面找，包括这个类本身也在其中
		String className = Context.get().getClassName(targetName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 3.如果是基本类型，基本类型数组，或者java.lang.下的类，则直接返回
		className = TypeTable.getClassName(simpleName);
		if (className != null)
			return className;

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");
	}

	public boolean addImport(String className) {

		// 如果是数组，则把修饰符号去掉
		String targetName = TypeUtils.getTargetName(className);
		String lastName = TypeUtils.getLastName(className);

		// 1. 基本类型不添加和java.lang.包下不添加
		if (TypeTable.isPrimitive(targetName) || targetName.equals("java.lang." + lastName))
			return true;

		// 2.如果是本身,不添加
		if (getClassName().equals(targetName))
			return true;

		// 3.如果引入了，则不必再添加了
		// 4.如果没有引入，但是typeName相同，则无法引入
		for (Import imp : imports) {
			if (!imp.hasAlias()) {
				if (imp.getClassName().equals(targetName)) {
					return true;

				} else if (imp.getLastName().equals(lastName)) {
					return false;
				}
			} else {// 如果是别名，则不用添加了
				if (imp.getClassName().equals(className))
					return false;
			}
		}
		imports.add(new Import(targetName));
		return true;
	}

	protected abstract String getClassName();

}
