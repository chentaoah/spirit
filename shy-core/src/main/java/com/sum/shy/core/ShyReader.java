package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.analyzer.SyntacticParser;
import com.sum.shy.core.api.Command;
import com.sum.shy.core.command.ClassCommand;
import com.sum.shy.core.command.DefCommand;
import com.sum.shy.core.command.FieldCommand;
import com.sum.shy.core.command.FuncCommand;
import com.sum.shy.core.command.ImportCommand;
import com.sum.shy.core.command.PackageCommand;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Result;

public class ShyReader {

	static {
		Command.register("package", new PackageCommand());
		Command.register("import", new ImportCommand());
		Command.register("def", new DefCommand());
		Command.register("class", new ClassCommand());
		Command.register("field", new FieldCommand());
		Command.register("func", new FuncCommand());
	}

	public Clazz read(File file) throws IOException {
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		// 打印一下
		for (String line : lines) {
			System.out.println(line);
		}
		return readLines(lines);
	}

	private Clazz readLines(List<String> lines) {

		Clazz clazz = new Clazz();
		Context context = Context.get();
		context.clazz = clazz;

		context.scope = "static";
		context.lines = lines;
		// 解析static域
		readScopeLines();

		context.scope = "class";
		context.lines = clazz.classLines;
		// 解析class域
		readScopeLines();

		return clazz;
	}

	private void readScopeLines() {
		// 获取所有行
		List<String> lines = Context.get().lines;
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			String line = lines.get(i);
			// 设置行号
			Context.get().lineNumber = i;
			Context.get().line = line;
			// 跳过注释
			if (line.trim().startsWith("//") || line.trim().length() == 0) {
				continue;
			}
			// 1.词法分析,将语句拆分成多个单元
			List<String> units = LexicalAnalyzer.analysis(line);
			// 2.语法分析,分析语句的语法
			String syntax = SyntacticParser.analysis(units);
			// 3.语义分析,根据语法,进行每个单元语义的分析
			Command command = Command.get(syntax);
			Result result = command.analysis(line, syntax, units);
			i = i + result.jump;
		}
	}

}
