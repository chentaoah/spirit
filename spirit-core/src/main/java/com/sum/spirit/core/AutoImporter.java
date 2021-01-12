package com.sum.spirit.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.build.SemanticParser;
import com.sum.spirit.core.visit.TypeNameVisiter;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Element;

@Component
public class AutoImporter {

	public static final String STRING_REGEX = "(?<=\").*?(?=\")";// 把字符串都替换掉
	public static final Pattern TYPE_PATTERN = Pattern.compile("(\\b[A-Z]+\\w+\\b)");// 间接排除了泛型类型T

	@Autowired
	public SemanticParser parser;

	public void autoImport(IClass clazz) {
		Set<String> classNames = dependencies(clazz);
		classNames.forEach(className -> clazz.addImport(className));
	}

	public Set<String> dependencies(IClass clazz) {
		Set<String> classNames = new HashSet<String>();
		// TODO 这里注解不能只简单获取名称，注解中也可能会有类型
		clazz.annotations.forEach((annotation) -> classNames.add(clazz.findClassName(annotation.getName())));
		classNames.addAll(visitElements(clazz, Arrays.asList(clazz.element)));
		classNames.remove(clazz.getClassName());
		return classNames;
	}

	public Set<String> visitElements(IClass clazz, List<Element> elements) {
		Set<String> classNames = new HashSet<>();
		for (Element element : elements) {
			String line = element.line.text;
			line = line.replaceAll(STRING_REGEX, "").trim();
			Matcher matcher = TYPE_PATTERN.matcher(line);
			while (matcher.find() && matcher.groupCount() > 0) {
				String targetName = matcher.group(matcher.groupCount() - 1);
				if (parser.isType(targetName)) {
					String className = clazz.findClassName(targetName);
					classNames.add(className);
				}
			}
			if (element.hasChild()) {
				classNames.addAll(visitElements(clazz, element.children));
			}
		}
		return classNames;
	}

	public String getFinalName(IClass clazz, IType type) {
		return new TypeNameVisiter().visitName(type, event -> {
			IType currentType = event.item;
			if (!clazz.addImport(currentType.getClassName())) {
				return currentType.getTypeName();
			}
			return null;
		});
	}

}
