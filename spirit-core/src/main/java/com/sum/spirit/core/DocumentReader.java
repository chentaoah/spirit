package com.sum.spirit.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.element.impl.Document;
import com.sum.spirit.pojo.element.impl.Element;
import com.sum.spirit.pojo.element.impl.Line;
import com.sum.spirit.pojo.element.impl.Statement;

import cn.hutool.core.io.IoUtil;

@Component
public class DocumentReader {

	@Autowired
	public ElementBuilder builder;

	public Document readDocument(String fileName, InputStream input) {
		Document document = new Document(fileName);
		List<String> lines = IoUtil.readLines(input, "UTF-8", new ArrayList<String>());
		doReadLines(lines, document);
		document.debug();// debug
		return document;
	}

	public void doReadLines(List<String> lines, Document document) {
		Stack<List<Element>> stack = new Stack<>();
		stack.push(document);
		for (int number = 0; number < lines.size(); number++) {
			String text = lines.get(number);
			// 创建行对象
			Line line = new Line(number + 1, text);
			if (line.isIgnore()) {
				continue;
			}
			// 创建元素对象
			Element element = builder.build(line);
			// what like "if xxx : xxx : xxx"
			List<String> sublines = splitLine(element);
			if (sublines != null && !sublines.isEmpty()) {
				lines.remove(number);
				lines.addAll(number, sublines);
				number--;
			} else {
				if (line.isEnding()) {
					stack.pop();
				}
				stack.peek().add(element);
				if (line.hasChild()) {
					stack.push(element.children);
				}
			}
		}
	}

	public List<String> splitLine(Element element) {
		if (element.isIf() || element.isFor() || element.isForIn() || element.isWhile()) {
			if (!element.contains(":")) {
				return null;
			}
			List<String> subLines = new ArrayList<>();
			List<Statement> statements = element.splitStmt(":");
			String indent = element.getIndent();
			subLines.add(indent + statements.get(0).toString() + " {");
			for (int i = 1; i < statements.size(); i++) {
				subLines.add(indent + "\t" + statements.get(i).toString());
			}
			subLines.add(indent + "}");
			return subLines;
		}
		return null;
	}
}
