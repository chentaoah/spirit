package com.sum.shy.sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

//语素
public class Morpheme {

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+$");
	public static final Pattern INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\([\\s\\S]*\\)$");

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
		} else if (INVOKE_PATTERN.matcher(str).matches()) {
			type = getInvokeType(str);
		} else if (ARRAY_PATTERN.matcher(str).matches()) {
			type = "array";
		} else if (MAP_PATTERN.matcher(str).matches()) {
			type = "map";
		} else if (VAR_PATTERN.matcher(str).matches()) {// 变量
			type = "var";
		}
		// 尝试从上下文中获取
		if ("var".equals(type)) {
			if (defTypes.containsKey(str)) {
				type = defTypes.get(str);
			}
		}
		return type;

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

	public static List<String> getGenericTypes(Map<String, String> defTypes, String type, String value) {
		List<String> genericTypes = new ArrayList<>();
		if ("array".equals(type)) {
			List<String> list = Splitter.on(CharMatcher.anyOf("[,]")).trimResults().splitToList(value);
			for (int i = 1; i < list.size() - 1; i++) {
				String elementType = getType(defTypes, list.get(i));
				if (!"var".equals(elementType)) {
					genericTypes.add(elementType);
					break;
				}
			}
			if (genericTypes.size() == 0) {
				genericTypes.add("var");
			}
		}
		if ("map".equals(type)) {
			List<String> list = Splitter.on(CharMatcher.anyOf("{,}")).trimResults().splitToList(value);
			for (int i = 1; i < list.size() - 1; i++) {
				String[] args = list.get(i).split(":");
				String keyType = getType(defTypes, args[0]);
				String valueType = getType(defTypes, args[1]);
				if (!"var".equals(keyType) && !"var".equals(valueType)) {
					genericTypes.add(keyType);
					genericTypes.add(valueType);
					break;
				}
			}
			if (genericTypes.size() == 0) {
				genericTypes.add("var");
				genericTypes.add("var");
			}
		}
		return genericTypes;
	}

}
