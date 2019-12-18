package com.sum.shy.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.core.analyzer.AutoImporter;
import com.sum.shy.core.analyzer.InvokeVisiter;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.utils.ReflectUtils;

public class ShyCompiler {

	public Map<String, CtClass> compile(Map<String, File> files) {

		// 设置所有的友元
		Context.get().friends = files.keySet();

		Map<String, CtClass> mainClasses = new LinkedHashMap<>();

		for (Map.Entry<String, File> entry : files.entrySet()) {
			String className = entry.getKey();
			File file = entry.getValue();
			// 读取类结构信息
			CtClass mainClass = new ShyReader().read(file);
			// 追加包名
			mainClass.packageStr = ReflectUtils.getPackage(className);
			// 自动引入友元,和常用的一些类
			AutoImporter.doImport(mainClass, file);
			// 将内部类当做普通的类,添加到集合中
			mainClasses.put(className, mainClass);

		}

		// 所有类,包括内部类
		Map<String, CtClass> allClasses = getAllClasses(mainClasses);
		// 设置上下文
		Context.get().classes = allClasses;
		// 推导出剩下未知的类型
		InvokeVisiter.visitClasses(allClasses);

		return mainClasses;
	}

	public Map<String, CtClass> getAllClasses(Map<String, CtClass> mainClasses) {

		Map<String, CtClass> classes = new LinkedHashMap<>();
		// 添加主类
		classes.putAll(mainClasses);
		// 添加内部类
		for (CtClass mainClass : mainClasses.values()) {
			for (CtClass innerClass : mainClass.innerClasses.values()) {
				classes.put(innerClass.getClassName(), innerClass);
			}
		}
		return classes;
	}

}
