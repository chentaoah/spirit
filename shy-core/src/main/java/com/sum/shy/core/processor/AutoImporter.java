package com.sum.shy.core.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.clazz.CtClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.utils.ReflectUtils;

/**
 * 自动引入器
 * 
 * @author chentao26275
 *
 */
public class AutoImporter {

	public static void doImport(CtClass clazz, File file) {
		try {
			// like Fatehr or G_Father
			Pattern pattern = Pattern.compile("(?!((?<=\").*?(?=\")))\\b[A-Z]+\\w+\\b");
			// 获取文件中的每一行
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			// 开始遍历
			for (int index = 0; index < fileLines.size(); index++) {
				String line = fileLines.get(index);
				if (StringUtils.isNotEmpty(line) && !line.trim().startsWith("//") && !line.trim().startsWith("package")
						&& !line.trim().startsWith("import")) {
					Matcher matcher = pattern.matcher(line);
					// 这里的find方法并不会一次找到所有的
					while (matcher.find()) {
						String typeName = matcher.group(matcher.groupCount() - 1);
						// 1.判断是否已经引入了，如果引入了，则不再重复引入
						if (!clazz.existImport(typeName)) {
							// clazz会返回一个合适的类名
							String className = findClassName(typeName);
							// 不为空，且不能是该类本身，则尝试添加
							if (className != null && !clazz.getClassName().equals(className)) {
								clazz.addImport(className);
								System.out.println("Automatically add a import info!class:[" + className + "]");
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// 分隔一下
		System.out.println();

	}

	/**
	 * 1.引入友元 2.引入List,Map
	 * 
	 * @param typeName
	 * @return
	 */
	public static String findClassName(String typeName) {
		// 友元，自身也在友元中
		String className = Context.get().findFriend(typeName);
		// 基本类型不用引入进来，一般集合引入进来就行
		if (className == null)
			className = ReflectUtils.getCollectionType(typeName);

		return className;

	}

}