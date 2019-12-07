package com.sum.shy.test;

public class Test {

	public static void main(String[] args) {
		String test = "G_Father father = \"\\\"G_Father\\\"\" + G_Father() +\"G_Father\"";
		test = test.replaceAll("(?!((?<=\").*?(?=\")))\\bG_Father\\b", "Child");
		System.out.println(test);

	}

}
