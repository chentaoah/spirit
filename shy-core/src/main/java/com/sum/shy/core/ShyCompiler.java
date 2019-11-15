package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;

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

		// 1. 解析
		Map<String, Clazz> classes = new LinkedHashMap<>();
		// 获取所有目录下的文件,并开始编译
		Map<String, File> files = new LinkedHashMap<>();
		recursiveFiles(files, "", path);
		for (Map.Entry<String, File> entry : files.entrySet()) {
			String className = entry.getKey();
			File file = entry.getValue();
			if (!debug) {
				Clazz clazz = resolve(className, file);
				classes.put(className, clazz);
			} else {
				debug(file);
			}
		}
		if (!debug) {
			// 设置到上下文中
			Context.get().classes = classes;
			// 2.构建java代码
			for (Clazz clazz : classes.values()) {
				compile(clazz);
			}
		}

	}

	public static Clazz resolve(String className, File file) {
		// 读取类结构信息
		Clazz clazz = new ShyReader().read(file);
		// 追加包名
		clazz.packageStr = className.substring(0, className.lastIndexOf("."));

		return clazz;
	}

	public static void debug(File file) {
		new ShyDebugger().read(file);
	}

	public static Class<?> compile(Clazz clazz) {

		// 转换方法中的内容,并生成java代码
		String text = new JavaBuilder().build(clazz);

		System.out.println(text);

		return null;

	}

	public static void recursiveFiles(Map<String, File> files, String packageStr, String path) {

		File dir = new File(path);
		if (!dir.isDirectory()) {
			return;
		}
		// 包名
		packageStr = packageStr + ("".equals(packageStr) ? "" : ".") + dir.getName();

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {// 递归
				recursiveFiles(files, packageStr, f.getAbsolutePath());
			} else if (f.isFile()) {// 如果是文件就添加
				files.put(packageStr + "." + f.getName().replace(".shy", ""), f);
			}
		}
	}

}
