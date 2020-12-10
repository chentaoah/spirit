package com.sum.spirit.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.build.SemanticParser;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.api.Annotated;
import com.sum.spirit.pojo.element.Element;

@Component
public class AutoImporter {

	public static final Pattern TYPE_PATTERN = Pattern.compile("(\\b[A-Z]+\\w+\\b)");// 间接排除了泛型类型T

	@Autowired
	public SemanticParser parser;

	public void visitClass(IClass clazz) {
		visitAnnotated(clazz, clazz);
		clazz.fields.forEach((field) -> visitAnnotated(clazz, field));
		clazz.methods.forEach((method) -> visitAnnotated(clazz, method));
	}

	public void visitAnnotated(IClass clazz, Annotated annotated) {
		annotated.annotations.forEach((annotation) -> clazz.addImport(clazz.findClassName(annotation.getName())));
		visitElement(clazz, annotated.element, annotated instanceof IMethod);
	}

	public void visitElement(IClass clazz, Element element, boolean visitChildren) {
		String line = element.line.text;
		line = line.replaceAll("(?<=\").*?(?=\")", "").trim(); // 把字符串都替换掉
		Matcher matcher = TYPE_PATTERN.matcher(line);
		while (matcher.find() && matcher.groupCount() > 0) {
			String targetName = matcher.group(matcher.groupCount() - 1);
			if (parser.isType(targetName)) {
				String className = clazz.findClassName(targetName);
				clazz.addImport(className);
			}
		}
		// 递归
		if (visitChildren) {
			element.children.forEach((child) -> visitElement(clazz, child, visitChildren));
		}
	}

}
