package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.lib.StringUtils;

public class AutoImporter {

	public static void doImport(Map<String, IClass> allClasses, Map<String, File> files) {
		for (Map.Entry<String, File> entry : files.entrySet()) {
			IClass clazz = allClasses.get(entry.getKey());
			doImport(clazz, entry.getValue());
		}
	}

	public static void doImport(IClass clazz, File file) {
		try {
			// 不在字符串内，并且大写开头的单词
			Pattern pattern = Pattern.compile("(\\b[A-Z]+\\w+\\b)");
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			for (int index = 0; index < fileLines.size(); index++) {
				String line = fileLines.get(index);
				line = line.replaceAll("(?<=\").*?(?=\")", "");// 把字符串都替换掉
				// 1.不为空 2.不是注释 3.不是包名 4.不是引入
				if (StringUtils.isNotEmpty(line) && !line.trim().startsWith("//") && !line.trim().startsWith("package")
						&& !line.trim().startsWith("import")) {
					Matcher matcher = pattern.matcher(line);
					while (matcher.find()) {// 这里的find方法并不会一次找到所有的
						if (matcher.groupCount() > 0) {
							String typeName = matcher.group(matcher.groupCount() - 1);
							String className = clazz.findImport(typeName);
							// 注意：主类添加引用，相当于协同类也会添加，因为共用了一个imports
							clazz.addImport(className);
							System.out.println("Automatically add a import info!class:[" + className + "]");
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
