package com.sum.spirit.core.element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.DocumentReader;
import com.sum.spirit.common.constants.Constants;
import com.sum.spirit.common.entity.Document;
import com.sum.spirit.common.entity.Element;
import com.sum.spirit.common.entity.Line;
import com.sum.spirit.common.entity.Statement;
import com.sum.spirit.common.utils.ConfigUtils;

import cn.hutool.core.io.IoUtil;

@Component
public class CoreDocumentReader implements DocumentReader {

	@Autowired
	public CoreElementBuilder builder;

	@Override
	public Document read(String fileName, InputStream input) {
		Document document = new Document(fileName);
		List<String> lines = IoUtil.readLines(input, Constants.DEFAULT_CHARSET, new ArrayList<String>());
		doReadLines(document, lines);
		if (ConfigUtils.isDebug()) {
			document.debug();
		}
		return document;
	}

	public void doReadLines(Document document, List<String> lines) {
		Stack<List<Element>> stack = new Stack<>();
		stack.push(document);
		for (int number = 0; number < lines.size(); number++) {
			String text = lines.get(number);
			Line line = new Line(number + 1, text);
			if (line.isIgnore()) {
				continue;
			}
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
