package com.sum.spirit.pojo.clazz;

import java.util.List;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

public abstract class Imported extends Annotated {

	public List<Import> imports;

	public Imported(List<Import> imports, List<IAnnotation> annotations, Element element) {
		super(annotations, element);
		this.imports = imports;
	}

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
		Compiler compiler = SpringUtils.getBean(Compiler.class);
		String className = compiler.getClassName(targetName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 3.如果是基本类型，基本类型数组，或者java.lang.下的类，则直接返回
		className = TypeEnum.getClassName(simpleName);
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
		if (TypeEnum.isPrimitive(targetName) || targetName.equals("java.lang." + lastName))
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

	public abstract String getClassName();

}
