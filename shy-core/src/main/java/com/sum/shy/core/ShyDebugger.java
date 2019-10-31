package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Stmt;

public class ShyDebugger {

	/**
	 * 这里仅仅测试一个语句被拆分成token以后,是什么样的,是否符合预期
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Clazz read(File file) throws IOException {
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		return readLines(lines);
	}

	private Clazz readLines(List<String> lines) {
		Context.get().scope = "method";
		// 获取所有行
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			String line = lines.get(i);
			// 跳过注释
			if (line.trim().startsWith("//") || line.trim().length() == 0) {
				System.out.println(line);
			} else {
				Stmt stmt = Stmt.create(line);
				System.out.println(
						line + getSpaceByNumber(45 - line.length()) + ">>> " + stmt.syntax + " " + stmt.debug());
			}

		}

		return null;
	}

	private String getSpaceByNumber(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

}
