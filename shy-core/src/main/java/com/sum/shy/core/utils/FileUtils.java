package com.sum.shy.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileUtils {

	public static void getFiles(String inputPath, String packageStr, Map<String, File> files) {

		File dir = new File(inputPath);
		if (!dir.isDirectory())
			return;
		// 包名
		packageStr = packageStr + ("".equals(packageStr) ? "" : ".") + dir.getName();

		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {// 递归
				getFiles(file.getAbsolutePath(), packageStr, files);

			} else if (file.isFile()) {// 如果是文件就添加
				if (file.getName().endsWith(".shy")) {
					files.put(packageStr + "." + file.getName().replace(".shy", ""), file);
				}
			}
		}
	}

	/**
	 * 生成.java文件
	 * 
	 * @param outputPath
	 * @param packageStr
	 * @param typeName
	 * @param code
	 */
	public static void generateFile(String outputPath, String packageStr, String typeName, String code) {
		// 分隔符
		String sep = File.separator;
		// 文件夹路径
		String dirPath = outputPath + sep + packageStr.replaceAll("\\.", "\\" + sep);
		// 文件名
		String filePath = dirPath + sep + typeName + ".java";
		File dir = new File(dirPath);
		File file = new File(filePath);
		try {
			// 文件夹不存在,创建文件夹
			if (!dir.exists())
				dir.mkdirs();
			// 文件不存在,则创建文件
			if (!file.exists())
				file.createNewFile();
			// 写出到文件
			Files.write(code, file, Charsets.UTF_8);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
