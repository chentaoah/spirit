package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.core.analyzer.AutoImporter;
import com.sum.shy.core.analyzer.InvokeVisiter;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.utils.FileUtils;

public class ShyCompiler {

	// 主方法
	public static void main(String[] args) throws IOException {

		// 是否debug
		boolean debug = false;

		String path = null;
		String OSName = System.getProperty("os.name");
		switch (OSName) {
		case "Windows 10":
			path = "D:\\Work\\CloudSpace\\Shy\\shy-core\\src\\main\\resources\\com.sum.test";
			break;
		case "Mac OS X":
			path = "/Users/chentao/Work/CloudSpace/Shy/shy-core/src/main/resources/com.sum.test";
			break;
		default:
			path = "D:\\Work\\CloudSpace\\Shy\\shy-core\\src\\main\\resources\\com.sum.test";
			break;
		}

		// 获取所有目录下的文件,并开始编译
		Map<String, File> files = new LinkedHashMap<>();
		Map<String, CtClass> classes = new LinkedHashMap<>();
		Context.get().files = files;
		Context.get().classes = classes;

		FileUtils.recursiveFiles(path, "", files);

		for (Map.Entry<String, File> entry : files.entrySet()) {
			String className = entry.getKey();
			File file = entry.getValue();
			if (!debug) {
				// 1.解析shy代码
				CtClass clazz = resolve(className, file);
				classes.put(className, clazz);
			} else {
				debug(file);
			}
		}

		// 推导出剩下未知的类型
		InvokeVisiter.visit(classes);
		// 自动引入友元
		AutoImporter.doImport(classes);

		if (!debug) {
			// 2.构建java代码
			for (CtClass clazz : classes.values()) {
				compile(clazz);
			}
		}

	}

	public static CtClass resolve(String className, File file) {
		// 读取类结构信息
		CtClass clazz = new ShyReader().read(file);
		// 追加包名
		clazz.packageStr = className.substring(0, className.lastIndexOf("."));

		// 展示一下
		clazz.show();

		return clazz;
	}

	public static void debug(File file) {
		new ShyDebugger().read(file);
	}

	public static Class<?> compile(CtClass clazz) {
		// 转换方法中的内容,并生成java代码
		String text = new JavaBuilder().build(clazz);
		System.out.println(text);
		return null;
	}

}
