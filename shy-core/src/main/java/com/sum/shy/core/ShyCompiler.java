package com.sum.shy.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.proc.AutoImporter;
import com.sum.shy.core.utils.TypeUtils;

public class ShyCompiler {

	public Map<String, IClass> compile(Map<String, File> files) {
		// 设置友元
		Context.get().friends = files.keySet();
		// 1.通过文件解析类信息
		Map<String, IClass> mainClasses = resolveMainClasses(files);
		// 2.从类里面取出所有内部类,供推导使用
		Map<String, IClass> allClasses = addCoopClasses(mainClasses);
		// 设置所有类
		Context.get().classes = allClasses;
		// 3.推导字段和方法的类型
		deriveTypeOfMembers(allClasses);

		return mainClasses;
	}

	public Map<String, IClass> resolveMainClasses(Map<String, File> files) {
		Map<String, IClass> mainClasses = new LinkedHashMap<>();
		for (Map.Entry<String, File> entry : files.entrySet()) {
			// 类名,通过文件路径推导可得
			String className = entry.getKey();
			File file = entry.getValue();
			// 读取类结构信息
			IClass mainClass = new ClassReader().read(file);
			// 追加包名
			mainClass.packageStr = TypeUtils.getPackage(className);
			// 自动引入友元,和常用的一些类
//			AutoImporter.doImport(mainClass, file);
			// 将内部类当做普通的类,添加到集合中
			mainClasses.put(className, mainClass);

		}
		return mainClasses;
	}

	public Map<String, IClass> addCoopClasses(Map<String, IClass> mainClasses) {
		Map<String, IClass> classes = new LinkedHashMap<>();
		classes.putAll(mainClasses);
		for (IClass mainClass : mainClasses.values()) {
			for (IClass coopClass : mainClass.coopClasses)
				classes.put(coopClass.getClassName(), coopClass);
		}
		return classes;
	}

	public void deriveTypeOfMembers(Map<String, IClass> allClasses) {
//		for (IClass clazz : allClasses.values()) {
//			for (AbsMember member : clazz.getAllMembers())
//				member.setType(InvokeVisiter.visitMember(clazz, member));
//		}
	}

}
