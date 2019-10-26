package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.api.ClassReader;
import com.sum.shy.api.Command;
import com.sum.shy.clazz.Clazz;
import com.sum.shy.command.ClassCommand;
import com.sum.shy.command.DefCommand;
import com.sum.shy.command.FieldCommand;
import com.sum.shy.command.FuncCommand;
import com.sum.shy.command.ImportCommand;
import com.sum.shy.command.PackageCommand;
import com.sum.shy.sentence.Sentence;

public class ShyReader implements ClassReader {

	static {
		Command.register("package", new PackageCommand());
		Command.register("import", new ImportCommand());
		Command.register("def", new DefCommand());
		Command.register("class", new ClassCommand());
		Command.register("field", new FieldCommand());
		Command.register("func", new FuncCommand());
	}

	@Override
	public Clazz readFile(File file) throws IOException {
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		return readLines(lines);
	}

	private Clazz readLines(List<String> lines) {

		// 打印一下
		for (String line : lines) {
			System.out.println(line);
		}

		Clazz clazz = new Clazz();
		// 快速读取文件的整体内容
		readScopeLines("static", clazz, lines);

		// 开始解析class结构
		readScopeLines("class", clazz, clazz.classLines);

		return clazz;
	}

	private void readScopeLines(String scope, Clazz clazz, List<String> lines) {

		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			String line = lines.get(i);
			// 跳过注释
			if (line.trim().startsWith("//") || line.trim().length() == 0) {
				continue;
			}
			// 根据一行字符串,生成对应的语句
			Sentence sentence = new Sentence(line);
			// 获取关键词
			String command = sentence.getCommand(scope);
			if (command == null || command.length() == 0) {
				continue;
			}
			// 获取指令
			Command cmd = Command.get(command);
			if (cmd != null) {
				// handle返回跳跃数
				i = i + cmd.handle(scope, clazz, lines, i, sentence);
			}

		}

	}

}
