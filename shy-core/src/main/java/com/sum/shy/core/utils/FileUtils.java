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

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {// 递归
				getFiles(f.getAbsolutePath(), packageStr, files);
			} else if (f.isFile()) {// 如果是文件就添加
				files.put(packageStr + "." + f.getName().replace(".shy", ""), f);
			}
		}
	}

}
