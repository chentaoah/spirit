package com.sum.shy.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.core.analyzer.AliasReplacer;
import com.sum.shy.core.analyzer.AutoImporter;
import com.sum.shy.core.analyzer.InvokeVisiter;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.utils.FileUtils;

public class ShyCompiler {

	// 主方法
	public static void main(String[] args) {

		// 第一个参数是代码地址
		String inputPath = args[0];
		// 第二参数是输出地址
		String outputPath = args[1];

		// debug模式可以观察词法，语法，和语义是否分析得正确
		boolean debug = false;

		// 获取所有目录下的文件,并开始编译
		Map<String, File> files = new LinkedHashMap<>();

		FileUtils.getFiles(inputPath, "", files);
		// 设置所有的友元
		Context.get().friends = files.keySet();

		Map<String, CtClass> mainClasses = new LinkedHashMap<>();
		for (Map.Entry<String, File> entry : files.entrySet()) {
			String className = entry.getKey();
			File file = entry.getValue();
			if (!debug) {
				// 1.解析shy代码
				CtClass mainClass = resolve(className, file);
				// 自动引入友元,和常用的一些类
				AutoImporter.doImport(mainClass, file);
				// 将内部类当做普通的类,添加到集合中
				mainClasses.put(className, mainClass);

			} else {
				debug(file);
			}
		}

		if (!debug) {
			// 所有类,包括内部类
			Map<String, CtClass> allClasses = getAllClasses(mainClasses);
			// 设置上下文
			Context.get().classes = allClasses;
			// 推导出剩下未知的类型
			InvokeVisiter.visitClasses(allClasses);

			for (CtClass clazz : mainClasses.values()) {
				// 2.构建java代码
				String code = build(clazz);
				// 输出到指定文件夹下
				FileUtils.generateFile(outputPath, clazz.packageStr, clazz.typeName, code);
			}

		}

	}

	public static CtClass resolve(String className, File file) {
		// 读取类结构信息
		CtClass clazz = new ShyReader().read(file);
		// 追加包名
		clazz.packageStr = className.substring(0, className.lastIndexOf("."));

		return clazz;
	}

	public static void debug(File file) {
		new ShyDebugger().read(file);
	}

	private static Map<String, CtClass> getAllClasses(Map<String, CtClass> mainClasses) {
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

	public static String build(CtClass clazz) {
		// 转换方法中的内容,并生成java代码
		String code = new JavaBuilder().build(clazz);
		// 替换类的别名
		code = AliasReplacer.replace(clazz, code);

		System.out.println(code);

		return code;
	}

}
