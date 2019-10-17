package com.sum.shy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Sentence {

	// 操作符
	public static final String[] SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">", "{", "}" };

	// 数组正则
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[ ]*=[ ]*\\[[a-zA-Z0-9\",]+\\]$");

	// 键值对正则
	public static final Pattern MAP_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[ ]*=[ ]*\\{[a-zA-Z0-9\",:]+\\}$");

	// 一行
	public String line;

	// 替换的字符串
	public Map<String, String> replacedStrs = new HashMap<>();

	// 拆分的语义单元
	public List<Unit> units = new ArrayList<>();

	public Sentence(String line) {
		this.line = line;
		// 1.将字符串,方法调用,数组,键值对,都当做一个整体来对待
		line = replaceString(line);
		System.out.println(line);
		line = replaceInvoke(line);
		System.out.println(line);
		line = replaceArray(line);
		System.out.println(line);
		line = replaceMap(line);
		System.out.println(line);
		// 2.处理操作符,添加空格,方便后面的拆分
		line = processSymbols(line);
		System.out.println(line);
		// 3.将多余的空格去掉
		line = removeSpace(line);
		System.out.println(line);
		// 4.根据操作符,进行拆分
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

		// 这里必须逆序遍历
		for (int i = list.size() - 1, count = list.size() - 1; i >= 0; i--) {
			Pair pair = list.get(i);
			// 截取字符串
			String str = line.substring(pair.start, pair.end + 1);
			line = line.replace(str, "$str" + count);
			replacedStrs.put("$str" + count--, str);
		}

		return line;
	}

	// 替换方法调用
	private String replaceInvoke(String line) {

		for (int i = 0, num = 0; i < line.length(); i++) {
			if (line.charAt(i) == '(') {
				int start = -1;
				for (int j = i - 1; j >= 0; j--) {
					if (!Character.isLetter(line.charAt(j))) {
						start = j + 1;
					}
				}
				int end = -1;
				for (int j = i + 1, count = 1; j < line.length(); j++) {
					if (line.charAt(j) == '(') {
						count++;
					} else if (line.charAt(j) == ')') {
						count--;
					}
					if (count == 0) {
						end = j;
						break;
					}
				}
				String str = line.substring(start, end + 1);
				String str1 = "$invoke" + num++;
				line = line.replace(str, str1);
				replacedStrs.put(str1, str);
				// 替换会影响索引,所以重新计算
				i = end - (str.length() - str1.length());

			}

		}

		return line;
	}

	private String replaceArray(String line) {
		if (ARRAY_PATTERN.matcher(line).matches()) {
			String str = line.substring(line.indexOf("["), line.indexOf("]") + 1);
			line = line.replace(str, "$array0");
			replacedStrs.put("$array0", str);
		}
		return line;
	}

	private String replaceMap(String line) {
		if (MAP_PATTERN.matcher(line).matches()) {
			String str = line.substring(line.indexOf("{"), line.indexOf("}") + 1);
			line = line.replace(str, "$map0");
			replacedStrs.put("$map0", str);
		}
		return line;
	}

	private String processSymbols(String line) {
		for (String str : SYMBOLS) {
			line = line.replaceAll(str, " " + str + " ");
		}
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
		for (String str : line.split(" ")) {
			units.add(new Unit(line, str));
		}
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
