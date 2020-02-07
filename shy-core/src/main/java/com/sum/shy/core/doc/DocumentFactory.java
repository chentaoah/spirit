package com.sum.shy.core.doc;

import java.io.File;
import java.util.List;

import com.sum.shy.core.utils.LineUtils;

public class DocumentFactory {

	public static Document create(File file, List<Line> lines) {
		Document document = new Document(file);
		readLines(lines, document);
		return document;
	}

	private static void readLines(List<Line> lines, List<Element> father) {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;
			i += readLine(lines, i, father, line);
		}
	}

	public static int readLine(List<Line> lines, int index, List<Element> father, Line line) {
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
