package com.sum.shy.test;

public class Test {

	public static void main(String[] args) {
		String test = "interface Inter extends Father ,Mather{";
		test = test.replaceAll("(?!((?<=\").*?(?=\")))\\b[A-Z]+\\w+\\b", "Child");
		System.out.println(test);

	}

}
