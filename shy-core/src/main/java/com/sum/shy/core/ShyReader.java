package com.sum.shy.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.parser.AnnotationParser;
import com.sum.shy.core.parser.ClassParser;
import com.sum.shy.core.parser.DeclareParser;
import com.sum.shy.core.parser.FieldParser;
import com.sum.shy.core.parser.FuncParser;
import com.sum.shy.core.parser.ImportParser;
import com.sum.shy.core.parser.InterfaceParser;
import com.sum.shy.core.parser.PackageParser;
import com.sum.shy.core.utils.TypeUtils;

public class ShyReader {

	static {
		Parser.register("package", new PackageParser());
		Parser.register("import", new ImportParser());
		Parser.register("annotation", new AnnotationParser());// 注解
		Parser.register("interface", new InterfaceParser());
		Parser.register("abstract", new ClassParser());
		Parser.register("class", new ClassParser());
		Parser.register("declare", new DeclareParser());// 声明
		Parser.register("assign", new FieldParser());
		Parser.register("func", new FuncParser());
	}

	public CtClass read(File file) {
		System.out.println("=================================== Shy ========================================");
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
		// 这里有一个问题,如果要支持一个文件里面多个class的话,那么这里就需要解析多次
		CtClass mainClass = new CtClass();
		// 文件名即类名,如果类名和文件名不一致,则认为是该类的内部类
		mainClass.typeName = TypeUtils.getTypeNameByFile(file);
		// 读取类的信息,包括静态方法,静态变量
		readScopeLines(mainClass, Constants.STATIC_SCOPE, lines);
		// 如果不是接口的话
		if (!mainClass.isInterface()) {
			// 继续读取类内部的信息
			readScopeLines(mainClass, Constants.CLASS_SCOPE, mainClass.classLines);
		}
		// 遍历读取内部类的信息
		for (CtClass innerClass : mainClass.innerClasses.values()) {
			readScopeLines(innerClass, Constants.CLASS_SCOPE, innerClass.classLines);
		}

		return mainClass;
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
