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
		String type = "var";
		// 从头开始遍历，直接从参数名，开始分析
		for (int i = 0; i < sentence.units.size(); i++) {
			type = getType(defTypes, sentence.getUnit(i));
			if (!"var".equals(type)) {
				break;
			}
		}
		return type;

	}

	public static String getType(Map<String, String> defTypes, String str) {
		String type = "var";
		if (BOOLEAN_PATTERN.matcher(str).matches()) {
			type = "boolean";
		} else if (INT_PATTERN.matcher(str).matches()) {
			type = "int";
		} else if (DOUBLE_PATTERN.matcher(str).matches()) {
			type = "double";
		} else if (STR_PATTERN.matcher(str).matches()) {
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

	public static List<String> getGenericTypes(Map<String, String> defTypes, String type, Sentence sentence) {
		List<String> genericTypes = new ArrayList<>();
		if ("array".equals(type)) {
			String genericType = getType(defTypes, (Sentence) sentence.getSubSentence(2));
			genericTypes.add(genericType);
		}
		if ("map".equals(type)) {
			genericTypes.add("var");
			genericTypes.add("var");
			Sentence subSentence = sentence.getSubSentence(2);
			boolean flag = true;
			for (int i = 0; i < subSentence.units.size(); i++) {
				String unit = subSentence.getUnit(i);
				if (":".equals(unit)) {
					flag = false;
				} else if (",".equals(unit)) {
					flag = true;
				}
				String genericType = getType(defTypes, unit);
				if (!"var".equals(genericType)) {
					genericTypes.set(flag ? 0 : 1, genericType);
					if (!"var".equals(genericTypes.get(0)) && !"var".equals(genericTypes.get(1))) {
						break;
					}
				}
			}
		}
		return genericTypes;
	}

}
