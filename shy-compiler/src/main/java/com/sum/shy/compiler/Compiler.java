package com.sum.shy.compiler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.api.Command;
import com.sum.shy.command.ClassCommand;
import com.sum.shy.command.FuncCommand;
import com.sum.shy.command.ImportCommand;
import com.sum.shy.command.VarCommand;
import com.sum.shy.command.PackageCommand;
import com.sum.shy.command.RefCommand;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class Compiler {

	static {
		Command.register("package", new PackageCommand());
		Command.register("import", new ImportCommand());
		Command.register("class", new ClassCommand());
		Command.register("var", new VarCommand());
		Command.register("func", new FuncCommand());
		Command.register("ref", new RefCommand());

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
		readScopeLines(clazz, null, "static", lines);
		// 开始解析class结构
		readScopeLines(clazz, null, "class", clazz.classLines);
		// 静态方法
		for (SMethod method : clazz.staticMethods) {
			readScopeLines(clazz, method, "method", method.methodLines);
		}
		// 成员方法
		for (SMethod method : clazz.methods) {
			readScopeLines(clazz, method, "method", method.methodLines);
		}
		return clazz;

	}

	private static void readScopeLines(SClass clazz, SMethod method, String scope, List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			// 取出第一个单词,判断是否在关键字中
			String line = lines.get(i);
			// 跳过注释
			if (line.startsWith("//")) {
				continue;
			}
			String newline = line.trim().replaceAll("=", " ");
			String keyword = line.substring(0, newline.indexOf(" "));
			Command command = Command.get(keyword);
			if (command == null) {
				command = Command.get("var");
			}
			// handle返回跳跃数
			i = i + command.handle(clazz, method, scope, lines, i, line);
		}
	}

	private static void convertClass(SClass clazz) {
		// TODO Auto-generated method stub

	}
}
