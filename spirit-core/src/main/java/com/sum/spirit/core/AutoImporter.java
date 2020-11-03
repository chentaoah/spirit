package com.sum.spirit.core;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.enums.KeywordEnum;

@Component
public class AutoImporter {

	public static final Pattern TYPE_PATTERN = Pattern.compile("(\\b[A-Z]+\\w+\\b)");

	public void doImport(Map<String, File> files, Map<String, IClass> allClasses) {
		for (Map.Entry<String, File> entry : files.entrySet()) {
			IClass clazz = allClasses.get(entry.getKey());
			doImport(clazz, entry.getValue());
		}
	}

	public void doImport(IClass clazz, File file) {
		try {
			// 不在字符串内，并且大写开头的单词
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			// 遍历每一行
			for (int index = 0; index < fileLines.size(); index++) {
				// 获取一行
				String line = fileLines.get(index);
				// 把字符串都替换掉
				line = line.replaceAll("(?<=\").*?(?=\")", "").trim();
				// 1.空 2.注释
				if (StringUtils.isEmpty(line) || line.startsWith("//"))
					continue;
				// 3.包名 4.引入
				if (line.startsWith(KeywordEnum.PACKAGE.value) || line.startsWith(KeywordEnum.IMPORT.value))
					continue;
				// 找到大写开头的
				Matcher matcher = TYPE_PATTERN.matcher(line);
				// 这里的find方法并不会一次找到所有的
				while (matcher.find() && matcher.groupCount() > 0) {
					// 找到大写的
					String targetName = matcher.group(matcher.groupCount() - 1);
					// 查询类名
					String className = clazz.findClassName(targetName);
					// 注意：主类添加引用，相当于协同类也会添加，因为共用了一个imports
					clazz.addImport(className);
				}
			}
		} catch (Exception e) {
			new RuntimeException("Auto import failed!", e);
		}
	}

}
