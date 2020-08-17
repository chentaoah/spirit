package com.sum.jass.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.pisces.core.ProxyFactory;
import com.sum.jass.api.DocumentReader;
import com.sum.jass.api.lexer.ElementBuilder;
import com.sum.jass.pojo.element.Document;
import com.sum.jass.pojo.element.Element;
import com.sum.jass.pojo.element.Line;
import com.sum.jass.pojo.element.Statement;
import com.sum.jass.utils.LineUtils;

public class DocumentReaderImpl implements DocumentReader {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	@Override
	public Document read(File file) {
		try {
			// 1.生成docment文档
			Document document = new Document(file);
			// 2.读取文件的每行
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			// 3.将文件的行转换成line对象
			List<Line> lines = convertLines(fileLines);
			// 4.读取每行的内容，并组成节点结构
			readLines(document, lines);

			return document;

		} catch (IOException e) {
			throw new RuntimeException("Fail to read file!", e);
		}
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
		Element element = builder.build(line);
		List<Line> sublines = splitLine(element);// what like "if xxx : xxx : xxx"
		if (sublines != null && sublines.size() > 0) {
			readLines(father, sublines);
			return 0;// 这里返回了，则不再执行doReadLine
		}
		return doReadLine(father, lines, index, element);
	}

	public List<Line> splitLine(Element element) {
		// 这几种语法可以合并成一行
		if (element.isFor() || element.isForIn() || element.isWhile() || element.isIf()) {
			if (element.contains(":")) {
				List<Line> subLines = new ArrayList<>();
				List<Statement> subStmts = element.split(":");
				String indent = element.getIndent();// 获取缩进
				subLines.add(new Line(indent + subStmts.get(0).toString() + " {"));// 第一行，添加后缀分隔
				for (int i = 1; i < subStmts.size(); i++)
					subLines.add(new Line(indent + "\t" + subStmts.get(i).toString()));
				subLines.add(new Line(indent + "}"));
				return subLines;
			}
		}
		return null;
	}

	public int doReadLine(List<Element> father, List<Line> lines, int index, Element element) {
		if (father != null)
			father.add(element);
		if (element.hasChild()) {
			List<Line> subLines = getSubLines(lines, index);// 解析子行
			readLines(element.children, subLines);
			return subLines.size();
		}
		return 0;
	}

	public List<Line> getSubLines(List<Line> lines, int index) {

		List<Line> subLines = new ArrayList<>();
		for (int i = index + 1, count = 1; i < lines.size(); i++) {

			String text = lines.get(i).text;
			// Flag in string or not
			boolean isInString = false;

			for (int j = 0; j < text.length(); j++) {
				char c = text.charAt(j);

				// If it's a quotation mark, and it's not escaped
				if (c == '"' && LineUtils.isNotEscaped(text, j))
					isInString = !isInString;

				// Count specific characters
				if (!isInString) {
					if (c == '{') {
						count++;
					} else if (c == '}') {
						count--;
						if (count == 0)
							return subLines;
					}
				}
			}

			if (count == 0)
				break;
			subLines.add(lines.get(i));
		}

		return subLines;
	}

}
