package com.sum.spirit.pojo.clazz;

import java.util.List;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.lib.StringUtils;
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

	public Import findImport(String simpleName) {
		for (Import imp : imports) {
			if (imp.matchSimpleName(simpleName))
				return imp;
		}
		return null;
	}

	public Import findImportByClassName(String className) {
		for (Import imp : imports) {
			if (imp.matchClassName(className))
				return imp;
		}
		return null;
	}

	public String getClassNameByClassLoader(String simpleName) {
		List<ClassLoader> classLoaders = SpringUtils.getBeansAndSort(ClassLoader.class);
		for (ClassLoader classLoader : classLoaders) {
			String className = classLoader.getClassName(simpleName);
			if (StringUtils.isNotEmpty(className))
				return className;
		}
		return null;
	}

	public boolean shouldImport(String className) {
		List<ClassLoader> classLoaders = SpringUtils.getBeansAndSort(ClassLoader.class);
		for (ClassLoader classLoader : classLoaders) {
			if (classLoader.isLoaded(className))
				return classLoader.shouldImport(className);
		}
		return true;
	}

	public String findClassName(String simpleName) {
		// 如果是className，则直接返回
		if (simpleName.contains("."))
			return simpleName;

		// 1.如果是基本类型，基本类型数组
		String className1 = TypeEnum.getClassName(simpleName);
		if (className1 != null)
			return className1;

		// 如果传进来是个数组，那么处理一下
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);

		// 2.首先先去引入里面找
		Import imp = findImport(targetName);
		String className2 = imp != null ? imp.getClassName() : null;
		if (className2 != null)
			return TypeUtils.getClassName(isArray, className2);

		// 3.使用类加载器，进行查询
		String className3 = getClassNameByClassLoader(targetName);
		if (className3 != null)
			return TypeUtils.getClassName(isArray, className3);

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");
	}

	public boolean addImport(String className) {
		// 如果是数组，则把修饰符号去掉
		String targetName = TypeUtils.getTargetName(className);
		String lastName = TypeUtils.getLastName(className);

		// 1.如果是本身,不添加
		if (getClassName().equals(targetName))
			return true;

		// 2. 原始类型不添加
		if (TypeEnum.isPrimitive(targetName))
			return true;

		// 3.基础类型或拓展类型不添加
		if (!shouldImport(targetName))
			return true;

		// 4.如果引入了，则不必引入了
		Import imp = findImportByClassName(targetName);
		if (imp != null)
			return !imp.hasAlias() ? true : false;

		// 5.如果存在简称相同的，则也不能引入
		Import imp1 = findImport(lastName);
		if (imp1 != null)
			return false;

		imports.add(new Import(targetName));
		return true;
	}

	public abstract String getClassName();

}
