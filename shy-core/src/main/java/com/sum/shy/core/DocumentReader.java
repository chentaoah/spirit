package com.sum.shy.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.shy.core.document.Document;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Line;
import com.sum.shy.core.utils.LineUtils;

public class DocumentReader {

	public Document read(File file) {
		try {
			// 1.生成docment文档
			Document document = new Document(file);
			// 2.读取文件的每行
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			// 3.将文件的行转换成line对象
			List<Line> lines = convertLines(fileLines);
			readLines(document, lines);
			return document;

		} catch (IOException e) {
			// ignore
		}
		return null;
	}

	public List<Line> convertLines(List<String> fileLines) {
		List<Line> lines = new ArrayList<>();
		for (int i = 0; i < fileLines.size(); i++) {
			String text = fileLines.get(i);
			Line line = new Line(i + 1, text);
			lines.add(line);
		}
		return lines;
	}

	public void readLines(List<Element> father, List<Line> lines) {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;
			i += readLine(father, lines, i, line);
		}
	}

	public int readLine(List<Element> father, List<Line> lines, int index, Line line) {
		// 1.解析一行
		Element element = new Element(line);
		if (father != null)
			father.add(element);
		if (element.hasChild()) {
			// 2.解析子行
			List<Line> subLines = LineUtils.getSubLines(lines, index);
			readLines(element, subLines);
			return subLines.size();
		}
		return 0;
	}

}
