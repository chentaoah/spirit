package com.sum.spirit.core.clazz.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sum.spirit.common.enums.PrimitiveEnum;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.element.entity.Element;

import cn.hutool.core.lang.Assert;

public abstract class ImportEntity extends ContextEntity {

	public List<Import> imports;

	public ImportEntity(List<Import> imports, List<IAnnotation> annotations, Element element) {
		super(annotations, element);
		this.imports = imports != null ? new ArrayList<>(imports) : new ArrayList<>();
	}

	public List<Import> getImports() {
		return imports.stream().filter(imp -> !imp.hasAlias()).collect(Collectors.toList());
	}

	public List<Import> getAliasImports() {
		return imports.stream().filter(imp -> imp.hasAlias()).collect(Collectors.toList());
	}

	public Import findImport(String className) {
		return ListUtils.findOne(imports, import0 -> import0.matchClassName(className));
	}

	public Import findImportByLastName(String simpleName) {
		return ListUtils.findOne(imports, import0 -> import0.matchSimpleName(simpleName));
	}

	public String findClassName(String simpleName) {
		// 校验
		Assert.notContain(simpleName, ".", "Simple name cannot contains \".\"");

		// 如果传进来是个数组，那么处理一下
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);

		// 1.如果是基本类型，基本类型数组
		String className = PrimitiveEnum.tryGetClassName(simpleName);

		// 2.首先先去引入里面找
		if (className == null) {
			Import import0 = findImportByLastName(targetName);
			className = import0 != null ? import0.getClassName() : null;
			className = className != null ? TypeUtils.getClassName(isArray, className) : null;
		}

		// 3.使用类加载器，进行查询
		if (className == null) {
			className = findClassNameByLoader(targetName);
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
		if (PrimitiveEnum.isPrimitive(targetName)) {
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

	public String findClassNameByLoader(String simpleName) {
		return ListUtils.collectOne(context.getImportSelectors(), importSelector -> importSelector.findClassName(simpleName));
	}

	public boolean shouldImport(String selfName, String className) {
		Boolean flag = ListUtils.collectOne(context.getImportSelectors(), importSelector -> importSelector.isHandle(className),
				importSelector -> importSelector.shouldImport(selfName, className));
		return flag == null ? true : flag;
	}

	public abstract String getClassName();

}
