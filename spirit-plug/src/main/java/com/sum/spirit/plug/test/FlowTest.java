package com.sum.spirit.plug.test;

import java.util.Arrays;
import java.util.List;

public class FlowTest {

	public static void main(String[] args) {
		String text = "START --> B[\"a = service1.doSomething()\"]\r\n" + //
				"	START --> D[\"b = service2.doSomething(a)\"]\r\n" + //
				"	B -->|a == 1| D\r\n" + //
				"	B -->|a == 2| E[\"service3.doSomething()\"]\r\n" + //
				"	B -->|a == 3| F[\"service4.doSomething()\"]\r\n" + //
				"	E --> F\r\n" + //
				"	F --> G[\"hello\"]\r\n" + //
				"	D --> END\r\n" + //
				"	G --> END\r\n" + //
				"	F --> END";//
		List<String> lines = Arrays.asList(text.split("\r\n"));
		FlowLexer lexer = new FlowLexer();
		for (String line : lines) {
			List<String> words = lexer.getWords(line);
			System.out.println(words);
		}
	}

}
