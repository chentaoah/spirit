package com.sum.shy.core.analyzer;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtClass;

/**
 * 自动引入器
 * 
 * @author chentao26275
 *
 */
public class FriendImporter {

	public static void doImport(CtClass clazz, File file) {
		try {
			// like Fatehr or G_Father
			Pattern pattern = Pattern.compile("(?!((?<=\").*?(?=\")))\\b[A-Z]+\\w+\\b");
			// 获取文件中的每一行
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			// 开始遍历
			for (int index = 0; index < fileLines.size(); index++) {
				String line = fileLines.get(index);
				// 忽略这两种
				if (!line.trim().startsWith("package") && !line.trim().startsWith("import")) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						for (int i = 0; i < matcher.groupCount(); i++) {
							String typeName = matcher.group(i);
							// 1.判断是否已经引入了，如果引入了，则不再重复引入
							if (!clazz.existImport(typeName)) {
								// 判断是否友元，这里还要判断一下是因为也可能是Object之类的常用类型
								String className = clazz.findClassName(typeName);
								if (Context.get().isFriend(className)) {
									clazz.addImport(className);
									System.out.println("Add a import info!class:" + className);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
