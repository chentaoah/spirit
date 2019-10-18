package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.api.Command;
import com.sum.shy.command.ClassCommand;
import com.sum.shy.command.FuncCommand;
import com.sum.shy.command.ImportCommand;
import com.sum.shy.command.FieldCommand;
import com.sum.shy.command.PackageCommand;
import com.sum.shy.converter.JavaConverter;
import com.sum.shy.entity.SClass;

public class Compiler {

	static {
		Command.register("package", new PackageCommand());
		Command.register("import", new ImportCommand());
		Command.register("class", new ClassCommand());
		Command.register("field", new FieldCommand());
		Command.register("func", new FuncCommand());
	}

	// 主方法
	public static void main(String[] args) throws IOException {
		// 1.读取文件
		String path = "xxxx";
		File dir = new File(path);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				List<String> lines = Files.readLines(file, Charsets.UTF_8);
				// 2.读取数据结构
				SClass clazz = readLines(lines);
				// 3.转换成对应代码
				convertClass(clazz);
			}
		}
	}

	private static SClass readLines(List<String> lines) {

		SClass clazz = new SClass();
		// 快速读取文件的整体内容
		readScopeLines("static", clazz, lines);
		// 开始解析class结构
		readScopeLines("class", clazz, clazz.classLines);

		return clazz;
	}

	private static void readScopeLines(String scope, SClass clazz, List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			String line = lines.get(i);
			// 跳过注释
			if (line.startsWith("//")) {
				continue;
			}
			// 根据一行字符串,生成对应的语句
			Sentence sentence = new Sentence(line);
			// 获取关键词
			String keyword = sentence.getKeyword(scope);
			// 获取指令
			Command command = Command.get(keyword);
			if (command != null) {
				// handle返回跳跃数
				i = i + command.handle(scope, clazz, lines, i, sentence);
			}

		}

	}

	private static void convertClass(SClass clazz) {
		new JavaConverter().convert(clazz);
	}
}
