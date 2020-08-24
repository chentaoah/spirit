package com.sum.spirit.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.DocumentReader;
import com.sum.spirit.api.lexer.ElementBuilder;
import com.sum.spirit.pojo.element.Document;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;
import com.sum.spirit.pojo.element.Statement;

public class DocumentReaderImpl implements DocumentReader {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	@Override
	public Document read(File file) {
		try {
			Document document = new Document(file);
			List<String> fileLines = Files.readLines(file, Charsets.UTF_8);
			Stack<List<Element>> stack = new Stack<>();
			stack.push(document);
			for (int number = 0; number < fileLines.size(); number++) {
				String text = fileLines.get(number);
				// create line object
				Line line = new Line(number + 1, text);
				if (line.isIgnore())
					continue;
				// create element object
				Element element = builder.build(line);
				// what like "if xxx : xxx : xxx"
				List<String> sublines = splitLine(element);
				if (sublines != null && !sublines.isEmpty()) {
					fileLines.remove(number);
					fileLines.addAll(number, sublines);
					number--;
				} else {
					if (line.isEnding())
						stack.pop();
					stack.peek().add(element);
					if (line.hasChild())
						stack.push(element.children);
				}
			}
			return document;

		} catch (IOException e) {
			throw new RuntimeException("Fail to read file!", e);
		}
	}

	public List<String> splitLine(Element element) {
		if (element.isIf() || element.isFor() || element.isForIn() || element.isWhile()) {
			if (element.contains(":")) {
				List<String> subLines = new ArrayList<>();
				List<Statement> statements = element.split(":");
				String indent = element.getIndent();
				subLines.add(indent + statements.get(0).toString() + " {");
				for (int i = 1; i < statements.size(); i++)
					subLines.add(indent + "\t" + statements.get(i).toString());
				subLines.add(indent + "}");
				return subLines;
			}
		}
		return null;
	}
}
