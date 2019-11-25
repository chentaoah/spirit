package com.sum.shy.core.utils;

import java.io.File;
import java.util.Map;

public class FileUtils {

	public static void getFiles(String path, String packageStr, Map<String, File> files) {

		File dir = new File(path);
		if (!dir.isDirectory()) {
			return;
		}
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

}
