package com.sum.spirit.plug.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class FlowTest {

	public static final String ACCEPT_ALL = "ACCEPT_ALL";

	public static void main(String[] args) {

		String text = "START --> B[\"a = service1.doSomething()\"]\r\n" + //
				"	START --> D[\"b = service2.doSomething(a)\"]\r\n" + //
				"	B -->|a == 1| D\r\n" + //
				"	B -->|a == 2| E[\"service3.doSomething()\"]\r\n" + //
				"	B -->|a == 3| F[\"service4.doSomething()\"]\r\n" + //
				"	E --> F\r\n" + //
				"	F --> G[\"service5.doSomething()\"]\r\n" + //
				"	D --> END[\"return\"]\r\n" + //
				"	G --> END[\"return\"]\r\n" + //
				"	F -->|a == 4| END[\"return\"]";//

//		System.out.println(text);
		List<String> lines = Arrays.asList(text.split("\r\n"));
		FlowLexer lexer = new FlowLexer();
		Map<String, Component> components = new HashMap<>();
		List<Element> elements = new ArrayList<>();
		for (String line : lines) {
			List<String> words = lexer.getWords(line);
			Element element = new Element(words);
			elements.add(element);
		}
		for (Element element : elements) {// 统计所有节点，并且去重
			addOrRecover(components, element.leftComponent);
			addOrRecover(components, element.rightComponent);
		}
		for (Element element : elements) {// 分析依赖关系
			Component component = components.get(element.leftComponent.name);
			Condition condition = element.condition == null ? new Condition("|" + ACCEPT_ALL + "|") : element.condition;
			component.connections.put(condition, components.get(element.rightComponent.name));
		}
		StringBuilder builder = new StringBuilder();
		buildCode(builder, components.get("START"), "\t");
		System.out.println(builder);
	}

	public static void addOrRecover(Map<String, Component> components, Component component) {
		if (!components.containsKey(component.name)) {
			components.put(component.name, component);
		} else {
			if (StringUtils.isNotEmpty(component.expression)) {
				components.put(component.name, component);
			}
		}
	}

	public static void buildCode(StringBuilder builder, Component component, String indent) {
		for (Entry<Condition, Component> entry : component.connections.entrySet()) {
			if (ACCEPT_ALL.equals(entry.getKey().expression)) {
				builder.append(indent).append(entry.getValue().expression).append(";\n");
				buildCode(builder, entry.getValue(), indent);
			} else {
				builder.append(indent).append("if (").append(entry.getKey().expression).append(") {\n");
				builder.append(indent + "\t").append(entry.getValue().expression).append(";\n");
				buildCode(builder, entry.getValue(), indent + "\t");
				builder.append(indent).append("}\n");
			}
		}
	}

	public static class Element {

		public Component leftComponent;
		public Condition condition;
		public Component rightComponent;

		public Element(List<String> words) {
			if (words.size() == 3) {
				leftComponent = new Component(words.get(0));
				rightComponent = new Component(words.get(2));

			} else if (words.size() == 4) {
				leftComponent = new Component(words.get(0));
				condition = new Condition(words.get(2));
				rightComponent = new Component(words.get(3));
			}
		}

		@Override
		public String toString() {
			return leftComponent + " -->|" + condition + "| " + rightComponent;
		}

	}

	public static class Component {

		public String name;
		public String expression;
		public Map<Condition, Component> connections = new LinkedHashMap<>();

		public Component(String word) {
			if (word.contains("[")) {
				this.name = word.substring(0, word.indexOf('['));
				this.expression = word.substring(word.indexOf('"') + 1, word.lastIndexOf('"'));
			} else {
				this.name = word;
			}
		}

		@Override
		public String toString() {
			return name + "[" + expression + "]";
		}
	}

	public static class Condition {

		public String expression;

		public Condition(String word) {
			this.expression = word.substring(1, word.length() - 1);
		}

		@Override
		public String toString() {
			return expression;
		}

	}

}
