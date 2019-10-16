package com.sum.shy.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		// 数字
		String text = "abc = 12345";
		String regex = "^[a-zA-Z0-9]+[ ]*=[ ]*\\d+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		System.out.println(matcher.matches());

		// 浮点数
		String text1 = "abc = 0.00";
		String regex1 = "^[a-zA-Z0-9]+[ ]*=[ ]*\\d+\\.\\d+$";
		Pattern pattern1 = Pattern.compile(regex1);
		Matcher matcher1 = pattern1.matcher(text1);
		System.out.println(matcher1.matches());

		// 布尔值
		String text2 = "abc = true";
		String regex2 = "^[a-zA-Z0-9]+[ ]*=[ ]*(true|false)$";
		Pattern pattern2 = Pattern.compile(regex2);
		Matcher matcher2 = pattern2.matcher(text2);
		System.out.println(matcher2.matches());

		// 数组
		String text3 = "abc = [\"a\",\"b\",\"c\"]";
		String regex3 = "^[a-zA-Z0-9]+[ ]*=[ ]*\\[[a-zA-Z0-9\",]+\\]$";
		Pattern pattern3 = Pattern.compile(regex3);
		Matcher matcher3 = pattern3.matcher(text3);
		System.out.println(matcher3.matches());

		// 键值对
		String text4 = "abc = {a:b,b:c,c:d}";
		String regex4 = "^[a-zA-Z0-9]+[ ]*=[ ]*\\{[a-zA-Z0-9\",:]+\\}$";
		Pattern pattern4 = Pattern.compile(regex4);
		Matcher matcher4 = pattern4.matcher(text4);
		System.out.println(matcher4.matches());

		// 字符串
		String text5 = "abc = \"abcdefg\"";
		String regex5 = "^[a-zA-Z0-9]+[ ]*=[ ]*\"[\\s\\S]*\"$";
		Pattern pattern5 = Pattern.compile(regex5);
		Matcher matcher5 = pattern5.matcher(text5);
		System.out.println(matcher5.matches());

		// 变量赋值

		// 赋值操作
		
	}
}
