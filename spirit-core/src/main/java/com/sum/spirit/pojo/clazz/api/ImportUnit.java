package com.sum.spirit.pojo.clazz.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.api.ImportSelector;
import com.sum.spirit.pojo.clazz.impl.IAnnotation;
import com.sum.spirit.pojo.clazz.impl.Import;
import com.sum.spirit.pojo.element.impl.Element;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

import cn.hutool.core.lang.Assert;

public abstract class ImportUnit extends AnnotationUnit {

	public List<Import> imports;

	public ImportUnit(List<Import> imports, List<IAnnotation> annotations, Element element) {
		super(annotations, element);
		this.imports = imports != null ? new ArrayList<>(imports) : new ArrayList<>();
	}

	public List<Import> getImports() {
		return imports.stream().filter(imp -> !imp.hasAlias()).collect(Collectors.toList());
	}

	public List<Import> getAliasImports() {
		return imports.stream().filter(imp -> imp.hasAlias()).collect(Collectors.toList());
	}

	public String findClassName(String simpleName) {
		// 校验
		Assert.notContain(simpleName, ".", "Simple name cannot contains \".\"");

		// 如果传进来是个数组，那么处理一下
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);

		// 1.如果是基本类型，基本类型数组
		String className = TypeEnum.getClassName(simpleName);

		// 2.首先先去引入里面找
		if (className == null) {
			Import import0 = findImportByLastName(targetName);
			className = import0 != null ? import0.getClassName() : null;
			className = className != null ? TypeUtils.getClassName(isArray, className) : null;
		}

		// 3.使用类加载器，进行查询
		if (className == null) {
			className = findClassNameByClassLoader(targetName);
			className = className != null ? TypeUtils.getClassName(isArray, className) : null;
		}

		Assert.notNull(className, "No import info found!simpleName:[" + simpleName + "]");
		return className;
	}

	public boolean addImport(String className) {
		// 如果是数组，则把修饰符号去掉
		String targetName = TypeUtils.getTargetName(className);
		String lastName = TypeUtils.getLastName(className);

		// 1. 原始类型不添加
		if (TypeEnum.isPrimitive(targetName)) {
			return true;
		}

		// 2.如果引入了，则不必引入了
		Import import0 = findImport(targetName);
		if (import0 != null) {
			return !import0.hasAlias() ? true : false;
		}

		// 3.如果存在简称相同的，则也不能引入
		Import import1 = findImportByLastName(lastName);
		if (import1 != null) {
			return false;
		}

		// 4.基础类型或拓展类型不添加
		if (!shouldImport(getClassName(), targetName)) {
			return true;
		}

		imports.add(new Import(targetName));
		return true;
	}

	public Import findImport(String className) {
		for (Import import0 : imports) {
			if (import0.matchClassName(className)) {
				return import0;
			}
		}
		return null;
	}

	public Import findImportByLastName(String simpleName) {
		for (Import import0 : imports) {
			if (import0.matchSimpleName(simpleName)) {
				return import0;
			}
		}
		return null;
	}

	public String findClassNameByClassLoader(String simpleName) {
		List<ImportSelector> importSelectors = SpringUtils.getBeansAndSort(ImportSelector.class);
		for (ImportSelector importSelector : importSelectors) {
			String className = importSelector.findClassName(simpleName);
			if (StringUtils.isNotEmpty(className)) {
				return className;
			}
		}
		return null;
	}

	public boolean shouldImport(String selfName, String className) {
		List<ImportSelector> importSelectors = SpringUtils.getBeansAndSort(ImportSelector.class);
		for (ImportSelector importSelector : importSelectors) {
			if (importSelector instanceof ClassLoader) {
				if (((ClassLoader<?>) importSelector).contains(className)) {
					return importSelector.shouldImport(selfName, className);
				}
			} else {
				return importSelector.shouldImport(selfName, className);
			}
		}
		return true;
	}

	public abstract String getClassName();

}
