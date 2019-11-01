package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.parser.ClassParser;
import com.sum.shy.core.parser.DefParser;
import com.sum.shy.core.parser.FieldParser;
import com.sum.shy.core.parser.FuncParser;
import com.sum.shy.core.parser.ImportParser;
import com.sum.shy.core.parser.PackageParser;

public class ShyReader {

	static {
		Parser.register("package", new PackageParser());
		Parser.register("import", new ImportParser());
		Parser.register("def", new DefParser());
		Parser.register("class", new ClassParser());
		Parser.register("assignment", new FieldParser());
		Parser.register("func", new FuncParser());
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
		// 解析static域
		readScopeLines(clazz, "static", lines);

		context.scope = "class";
		// 解析class域
		readScopeLines(clazz, "class", clazz.classLines);

		return clazz;
	}

	private void readScopeLines(Clazz clazz, String scope, List<String> lines) {
		// 获取所有行
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			String line = lines.get(i);
			// 跳过注释
			if (line.trim().startsWith("//") || line.trim().length() == 0) {
				continue;
			}

			Stmt stmt = Stmt.create(line);
			Parser parser = Parser.get(stmt.syntax);
			int jump = parser.parse(clazz, scope, lines, i, line, stmt);
			i = i + jump;
		}
	}

}
