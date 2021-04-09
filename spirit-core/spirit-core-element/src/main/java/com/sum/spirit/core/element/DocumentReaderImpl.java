package com.sum.spirit.core.element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.constants.Constants;
import com.sum.spirit.common.utils.ConfigUtils;
import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.api.DocumentReader;
import com.sum.spirit.core.api.ElementBuilder;
import com.sum.spirit.core.element.entity.Document;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Line;
import com.sum.spirit.core.element.entity.Statement;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

@Component
public class DocumentReaderImpl implements DocumentReader {

	@Autowired
	public ElementBuilder builder;

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
		for (int index = 0; index < lines.size(); index++) {
			String text = lines.get(index);
			Line line = new Line(index + 1, text);
			if (line.isIgnore()) {
				continue;
			}
			Element element = builder.build(line);
			// what like "var = {"
			if (mergeLinesIfPossible(document, lines, index, element)) {
				index--;
				continue;
			}
			// what like "if xxx : xxx : xxx"
			List<String> sublines = splitLine(element);
			if (sublines != null && !sublines.isEmpty()) {
				lines.remove(index);
				lines.addAll(index, sublines);
				index--;

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

	public boolean mergeLinesIfPossible(Document document, List<String> lines, int startIndex, Element element) {
		if (element.isObjectAssign()) {
			StringBuilder builder = new StringBuilder(lines.get(startIndex));
			for (int index = startIndex + 1; index < lines.size(); index++) {
				builder.append(StrUtil.removeAny(lines.get(index), "\t").trim());
				int end = LineUtils.findEndIndex(builder, 0, '{', '}');
				if (end != -1) {
					Lists.remove(lines, startIndex, index + 1);
					lines.add(startIndex, builder.toString());
					return true;
				}
			}
		}
		return false;
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
