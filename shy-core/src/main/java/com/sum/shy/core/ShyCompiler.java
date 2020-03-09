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
		Map<String, IClass> allClasses = resolveClasses(files);
		Context.get().classes = allClasses;
		MemberVisiter.visit(allClasses);
		return allClasses;
	}

	public Map<String, IClass> resolveClasses(Map<String, File> files) {
		Map<String, IClass> allClasses = new LinkedHashMap<>();
		for (Map.Entry<String, File> entry : files.entrySet()) {
			// 获取包名
			String packageStr = TypeUtils.getPackage(entry.getKey());
			// 读取类结构信息
			List<IClass> classes = new ClassReader().read(packageStr, entry.getValue());
			// TODO 自动引入

			// 添加到集合中
			for (IClass clazz : classes)
				allClasses.put(clazz.getClassName(), clazz);

		}
		return allClasses;
	}

}
