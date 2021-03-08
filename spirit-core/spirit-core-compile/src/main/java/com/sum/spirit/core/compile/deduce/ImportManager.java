package com.sum.spirit.core.compile.deduce;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.PrimitiveEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.api.ImportSelector;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.clazz.utils.TypeUtils;

import cn.hutool.core.lang.Assert;

@Component
@DependsOn("springUtils")
public class ImportManager implements InitializingBean {

	public List<ImportSelector> importSelectors;

	@Override
	public void afterPropertiesSet() throws Exception {
		importSelectors = SpringUtils.getBeansAndSort(ImportSelector.class);
	}

	public String findClassName(IClass clazz, String simpleName) {
		// 校验
		Assert.notContain(simpleName, ".", "Simple name cannot contains \".\"");

		// 如果传进来是个数组，那么处理一下
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);

		// 1.如果是基本类型，基本类型数组
		String className = PrimitiveEnum.tryGetClassName(simpleName);

		// 2.首先先去引入里面找
		if (className == null) {
			Import import0 = clazz.findImportByLastName(targetName);
			className = import0 != null ? import0.getClassName() : null;
			className = className != null ? TypeUtils.getClassName(isArray, className) : null;
		}

		// 3.使用类加载器，进行查询
		if (className == null) {
			className = findClassName(targetName);
			className = className != null ? TypeUtils.getClassName(isArray, className) : null;
		}

		Assert.notNull(className, "No import info found!simpleName:[" + simpleName + "]");
		return className;
	}

	public boolean addImport(IClass clazz, String className) {
		// 如果是数组，则把修饰符号去掉
		String targetName = TypeUtils.getTargetName(className);
		String lastName = TypeUtils.getLastName(className);

		// 1. 原始类型不添加
		if (PrimitiveEnum.isPrimitive(targetName)) {
			return true;
		}

		// 2.如果引入了，则不必引入了
		Import import0 = clazz.findImport(targetName);
		if (import0 != null) {
			return !import0.hasAlias() ? true : false;
		}

		// 3.如果存在简称相同的，则也不能引入
		Import import1 = clazz.findImportByLastName(lastName);
		if (import1 != null) {
			return false;
		}

		// 4.基础类型或拓展类型不添加
		if (!shouldImport(clazz.getClassName(), targetName)) {
			return true;
		}

		clazz.imports.add(new Import(targetName));
		return true;
	}

	public String findClassName(String simpleName) {
		return Lists.collectOne(importSelectors, importSelector -> importSelector.findClassName(simpleName));
	}

	public boolean shouldImport(String selfName, String className) {
		Boolean flag = Lists.collectOne(importSelectors, importSelector -> importSelector.isHandle(className),
				importSelector -> importSelector.shouldImport(selfName, className));
		return flag == null ? true : flag;
	}

}
