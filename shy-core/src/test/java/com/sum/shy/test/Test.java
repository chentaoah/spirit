package com.sum.shy.test;

import java.util.List;

import com.sum.shy.core.analyzer.LexicalAnalyzer;

public class Test {

	public static void main(String[] args) {

		String text = "func main(){";

		List<String> list = LexicalAnalyzer.getWords(text);

		System.out.println(list);

	}

}
