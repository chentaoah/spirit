package com.sum.shy.sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Analyzer {

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+$");
	public static final Pattern INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\([\\s\\S]*\\)$");

	public static String getType(Map<String, String> defTypes, Sentence sentence) {
		for (String unit : sentence.units) {
			String type = Analyzer.getType(defTypes, unit);
			if (!"var".equals(type)) {
				return type;
			}
		}
		return "var";
	}

	public static String getType(Map<String, String> defTypes, String str) {
		String type = "var";
		if (isBoolean(str)) {
			type = "boolean";
		} else if (isInt(str)) {
			type = "int";
		} else if (isDouble(str)) {
			type = "double";
		} else if (isStr(str)) {
			type = "str";
		} else if (isInvoke(str)) {
			type = getInvokeType(str);
		} else if (isArray(str)) {
			type = "array";
		} else if (isMap(str)) {
			type = "map";
		} else if (VAR_PATTERN.matcher(str).matches()) {// 变量
			type = "var";
		}
		// 如果还是返回还是未知的,则通过名称来获取类型
		if ("var".equals(type)) {
			if (defTypes.containsKey(str)) {
				type = defTypes.get(str);
			}
		}
		return type;
	}

	public static List<String> getGenericTypes(Map<String, String> defTypes, String type, Sentence sentence) {
		List<String> genericTypes = new ArrayList<>();
		if ("array".equals(type)) {
			Sentence subSentence = sentence.getSubSentence(2);
			String genericType = getType(defTypes, subSentence);
			genericTypes.add(genericType);
			return genericTypes;
		} else if ("map".equals(type)) {
			genericTypes.add("var");
			genericTypes.add("var");
			Sentence subSentence = sentence.getSubSentence(2);
			boolean flag = true;
			for (int j = 0; j < subSentence.units.size(); j++) {
				String unit = subSentence.getUnit(j);
				if (":".equals(unit)) {
					flag = false;
				} else if (",".equals(unit)) {
					flag = true;
				}
				String genericType = Analyzer.getType(defTypes, unit);
				if (!"var".equals(genericType)) {
					genericTypes.set(flag ? 0 : 1, genericType);
					if (!"var".equals(genericTypes.get(0)) && !"var".equals(genericTypes.get(1))) {
						return genericTypes;
					}
				}
			}
		}

		return genericTypes;

	}

	public static boolean isBoolean(String str) {
		return BOOLEAN_PATTERN.matcher(str).matches();
	}

	public static boolean isInt(String str) {
		return INT_PATTERN.matcher(str).matches();
	}

	public static boolean isDouble(String str) {
		return DOUBLE_PATTERN.matcher(str).matches();
	}

	public static boolean isStr(String str) {
		return STR_PATTERN.matcher(str).matches();
	}

	public static boolean isInvoke(String str) {
		return INVOKE_PATTERN.matcher(str).matches();
	}

	public static boolean isArray(String str) {
		return ARRAY_PATTERN.matcher(str).matches();
	}

	public static boolean isMap(String str) {
		return MAP_PATTERN.matcher(str).matches();
	}

	private static String getInvokeType(String str) {
		// 构造函数
		if (isInitInvoke(str)) {
			return getInitMethod(str);
		}
		return "var";
	}

	public static boolean isInitInvoke(String str) {
		return INIT_PATTERN.matcher(str).matches();
	}

	public static String getInitMethod(String str) {
		return str.substring(0, str.indexOf("("));
	}

}
