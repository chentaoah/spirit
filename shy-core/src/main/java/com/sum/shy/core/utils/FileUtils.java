package com.sum.shy.core.utils;

import java.io.File;
import java.util.Map;

public class FileUtils {

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
