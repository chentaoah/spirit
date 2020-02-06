package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.clazz.IClass;
import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.doc.Line;
import com.sum.shy.utils.LineUtils;

public class ClassReader {

	public IClass read(File file) {
		try {
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			List<Line> lines = new ArrayList<>();
			// 1.遍历文件每行，转换成line对象
			for (int i = 0; i < fileLines.size(); i++) {
				String text = fileLines.get(i);
				Line line = new Line(i + 1, text);
				lines.add(line);
			}
			// 2.遍历每行line
			Document document = new Document(file);
			readLines(lines, document);
			// 3.打印日志
			document.debug();
			// 4.生成Class对象
			IClass clazz = new IClass(document);
			return clazz;

		} catch (IOException e) {
			// ignore
		}
		return null;

	}

	private void readLines(List<Line> lines, List<Element> father) {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;
			i += readLine(lines, i, father, line);
		}
	}

	public int readLine(List<Line> lines, int index, List<Element> father, Line line) {
		// 1.解析一行
		Element element = new Element(line);
		if (father != null)
			father.add(element);
		if (element.hasChild()) {
			// 2.解析子行
			List<Line> subLines = LineUtils.getSubLines(lines, index);
			readLines(subLines, element);
			return subLines.size();
		}
		return 0;
	}
}
