package com.sum.shy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {

	// 操作符
	public static final String[] OPERATOR = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">" };
	// 分隔符
	public static final String[] SEPARATOR = new String[] { " ", ",", ":" };

	// 括号
	public static final String[] BRACKETS = new String[] { "{", "}", "[", "]", "(", ")" };

	public String line;
	// 替换的字符串
	public Map<String, String> replacedStrs;
	// 拆分的语义单元
	public List<Unit> units;

	public Sentence(String line) {
		this.line = line;
		// 1.排除字符串的影响
		line = replaceString(line);
		// 2.将多余的空格去掉
		line = removeSpace(line);
		// 3.根据操作符,进行拆分
		splitString(line);

	}

	private String replaceString(String line) {
		// 1.排除字符串带来的影响
		List<Pair> list = new ArrayList<>();
		for (int i = 0, count = 0; i < line.length(); i++) {
			// 如果是字符串的边界,且没有被转义
			if (line.charAt(i) == '"' && line.charAt(i - 1 >= 0 ? i - 1 : i) != '\\') {
				count++;
				if (count % 2 != 0) {
					list.add(new Pair(i));
				} else {
					list.get(count / 2).end = i;
				}
			}
		}
		Map<String, String> map = new HashMap<>();
		for (int i = 0, count = 0; i < list.size(); i++) {
			Pair pair = list.get(i);
			// 截取字符串
			String str = line.substring(pair.start, pair.end + 1);
			map.put("$str" + count, str);
			line = line.replace(str, "$str" + count++);
		}
		replacedStrs = map;
		return line;
	}

	private String removeSpace(String line) {
		// 去掉首尾
		line = line.trim();
		while (line.contains("  ")) {
			line.replaceAll("  ", "");
		}
		return line;
	}

	private void splitString(String line) {

		List<Unit> list = new ArrayList<>();
		// 使用贪婪的模式
		int last = 0;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			// 如果是字符则继续遍历下去
			if (Character.isLetter(c)) {
				continue;
			} else {
				// 将之前的字符串截取出来
				// 如果是空格
				if (c == ' ') {
					String str = line.substring(last, i - 1);
					list.add(new Unit(str));
					last = i + 1;
				} else if (c == '=') {
					String str = line.substring(last, i - 1);
					list.add(new Unit(str));
					list.add(new Unit("=", "assignment"));
					last = i + 1;
				}

			}

		}

		// 1.先根据符号拆分
//		List<Unit> list = new ArrayList<>();
//		int last = 0;
//		for (int i = 0; i < line.length(); i++) {
//			char c = line.charAt(i);
//			// 如果不是字母和数字
//			if (!Character.isLetter(c) && !Character.isDigit(c)) {
//				// 符号之间的空格,这样的空格是多余的
//				if (c == ' ') {
//					char left = line.charAt(i - 1);
//					char right = line.charAt(i + 1);
//					if (Character.isLetter(left) && Character.isLetter(right)) {
//
//					}
//				}
//				if (c == '(') {
//					int start = -1;
//					for (int j = i - 1; j >= 0; j--) {
//						if (!Character.isLetter(line.charAt(j))) {
//							start = j + 1;
//						}
//					}
//					int end = -1;
//					for (int j = i + 1, count = 1; j < line.length(); j++) {
//						if (line.charAt(j) == '(') {
//							count++;
//						} else if (line.charAt(j) == ')') {
//							count--;
//						}
//						if (count == 0) {
//							end = j;
//							break;
//						}
//					}
//					list.add(new Unit(line.substring(start, end + 1), "invoke"));
//					last
//
//				}
//				// 这些开头的,可能是二目符号
//				boolean flag = (c == '<' || c == '>' || c == '=' || c == '!' || c == '&' || c == '|')
//						&& (i != line.length() - 1);
//				// 普通符号,先比对二目符号
//				for (int j = 0; j < OPERATOR.length; j++) {
//					String symbol = OPERATOR[j];
//					if (flag && symbol.length() == 2) {
//						if (c == symbol.charAt(0) && line.charAt(i + 1) == symbol.charAt(1)) {
//							list.add(new Unit(line.substring(last, i).trim()));
//							list.add(new Unit(symbol));
//							last = i + 2;
//							break;
//						}
//					} else if (symbol.length() == 1) {
//						if (c == symbol.charAt(0)) {
//							list.add(new Unit(line.substring(last, i).trim()));
//							list.add(new Unit(symbol));
//							last = i + 1;
//							break;
//						}
//					}
//				}
//			}
//		}

	}

	// 获取单元的字符串
	public String getUnitStr(int index) {
		return units.get(index).str;
	}

	public String getKeyword() {
		// 判断首个单词是否关键字
		String str = getUnitStr(0);
		if ("package".equals(str)) {
			return "package";
		} else if ("import".equals(str)) {
			return "import";
		} else if ("class".equals(str)) {
			return "class";
		} else if ("ref".equals(str)) {
			return "ref";
		} else if ("func".equals(str)) {
			return "func";
		} else if ("if".equals(str)) {
			return "if";
		} else if ("for".equals(str)) {
			return "for";
		} else if ("return".equals(str)) {
			return "return";
		} else {
			// 如果第二个语义是"=",那么可以认为是赋值语句
			str = getUnitStr(1);
			if ("=".equals(str)) {
				return "var";
			}
			// 如果只有右值的话,则只能返回调用
			return "invoke";
		}

	}

	// 获取自动变量的类型
	public String getVarType() {
		// 如果第二个语义是"=",那么可以认为是赋值语句
		String str = getUnitStr(1);
		if (!"=".equals(str)) {
			return null;
		}
		for (Unit unit : units) {

		}

		return null;
	}

	public class Pair {
		public int start;
		public int end;

		public Pair(int start) {
			this.start = start;
		}

	}

}
