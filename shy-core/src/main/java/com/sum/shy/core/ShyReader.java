package com.sum.shy.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.parser.AnnotationParser;
import com.sum.shy.core.parser.ClassParser;
import com.sum.shy.core.parser.DeclareParser;
import com.sum.shy.core.parser.FieldParser;
import com.sum.shy.core.parser.FuncParser;
import com.sum.shy.core.parser.ImportParser;
import com.sum.shy.core.parser.PackageParser;

public class ShyReader {

	static {
		Parser.register("package", new PackageParser());
		Parser.register("import", new ImportParser());
		Parser.register("annotation", new AnnotationParser());// 注解
		Parser.register("class", new ClassParser());
		Parser.register("declare", new DeclareParser());// 声明
		Parser.register("assign", new FieldParser());
		Parser.register("func", new FuncParser());
	}

	public CtClass read(File file) {
		try {
			// 获取文件中的每一行
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			List<Line> lines = new ArrayList<>();
			// 开始遍历
			for (int index = 0; index < fileLines.size(); index++) {
				lines.add(new Line(index + 1, fileLines.get(index)));
				System.out.println(lines.get(index).text);
			}
			return readLines(file, lines);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CtClass readLines(File file, List<Line> lines) {
		CtClass clazz = new CtClass();
		readScopeLines(clazz, "static", lines);
		readScopeLines(clazz, "class", clazz.classLines);
		// 如果文件中不存在class对象,那么就用文件名虚拟一个
		if (clazz.typeName == null) {
			clazz.typeName = file.getName().replace(".shy", "");
		}
		return clazz;
	}

	private void readScopeLines(CtClass clazz, String scope, List<Line> lines) {
		// 获取所有行
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);
			try {
				Parser parser = Parser.get(stmt.syntax);
				int jump = parser.parse(clazz, scope, lines, i, line, stmt);
				i = i + jump;
			} catch (Exception e) {
				System.out.println(stmt);
				System.out.println(stmt.syntax);
				throw e;
			}

		}

	}

}
