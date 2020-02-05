package com.sum.shy.core.document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.entity.Line;
import com.sum.shy.utils.LineUtils;

public class ClassReader {

	public static Document readFile(File file) throws IOException {
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
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;
			readLine(document, line, lines, i);
		}
		return document;
	}

	public static void readLine(List<Element> father, Line line, List<Line> lines, int index) {
		Element element = new Element(line);
		if (father != null)
			father.add(element);
		if (element.hasChild()) {
			List<Line> subLines = LineUtils.getSubLines(lines, index);
			for (int i = 0; i < subLines.size(); i++) {
				Line subLine = subLines.get(i);
				if (subLine.isIgnore())
					continue;
				readLine(element, subLine, lines, index + i + 1);
			}
		}
	}
}
