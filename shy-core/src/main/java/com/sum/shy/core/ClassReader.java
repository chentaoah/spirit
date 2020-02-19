package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Line;
import com.sum.shy.core.factory.ClassFactory;
import com.sum.shy.core.factory.DocumentFactory;

public class ClassReader {

	public IClass read(File file) {
		try {
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			// 1.遍历文件每行，转换成line对象
			List<Line> lines = convertFileLines(fileLines);
			// 2.遍历每行line
			Document document = DocumentFactory.create(file, lines);
			// 3.打印日志
			document.debug();
			// 4.生成Class对象
			return ClassFactory.create(document);

		} catch (IOException e) {
			// ignore
		}
		return null;

	}

	private List<Line> convertFileLines(List<String> fileLines) {
		List<Line> lines = new ArrayList<>();
		for (int i = 0; i < fileLines.size(); i++) {
			String text = fileLines.get(i);
			Line line = new Line(i + 1, text);
			lines.add(line);
		}
		return lines;
	}

}
