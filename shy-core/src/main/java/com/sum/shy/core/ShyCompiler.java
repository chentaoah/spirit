package com.sum.shy.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.utils.TypeUtils;

public class ShyCompiler {

	public Map<String, IClass> compile(Map<String, File> files) {
		// 1.初步解析所有的class对象
		Map<String, IClass> allClasses = resolveClasses(files);
		// 放到上下文
		Context.get().classes = allClasses;
		// 2.自动引入同个工程下的类，包括List,Map等常用集合
		AutoImporter.doImport(allClasses, files);
		// 3.推导字段和方法的返回类型
		MemberVisiter.visit(allClasses);

		return allClasses;
	}

	public Map<String, IClass> resolveClasses(Map<String, File> files) {
		Map<String, IClass> allClasses = new LinkedHashMap<>();
		for (Map.Entry<String, File> entry : files.entrySet()) {
			String packageStr = TypeUtils.getPackage(entry.getKey());
			List<IClass> classes = new ClassReader().read(packageStr, entry.getValue());
			for (IClass clazz : classes)
				allClasses.put(clazz.getClassName(), clazz);
		}
		return allClasses;
	}

}
