package com.sum.shy.core.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.core.api.Type;

public class CodeType implements Type {

	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9_]+$");
	public static final Pattern ARRAY_TYPE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+<[a-zA-Z0-9_<>,]+>$");

	public String category;// 类别
	public String type;// 类名
	public Map<String, String> genericTypes = new LinkedHashMap<>();// 泛型参数

	public CodeType(String type) {// 这里也不再支持多层嵌套的泛型了

		if ("bool".equals(type)) {
			this.category = Constants.BOOL_TYPE;
			this.type = "bool";

		} else if ("int".equals(type)) {
			this.category = Constants.INT_TYPE;
			this.type = "int";

		} else if ("long".equals(type)) {
			this.category = Constants.LONG_TYPE;
			this.type = "long";

		} else if ("double".equals(type)) {
			this.category = Constants.DOUBLE_TYPE;
			this.type = "double";

		} else if ("str".equals(type)) {
			this.category = Constants.STR_TYPE;
			this.type = "str";

		} else if ("obj".equals(type)) {
			this.category = Constants.OBJ_TYPE;
			this.type = "obj";

		} else if ("void".equals(type)) {
			this.category = Constants.VOID_TYPE;
			this.type = "void";

		} else if (TYPE_PATTERN.matcher(type).matches()) {
			this.category = Constants.CLASS_TYPE;
			this.type = type;

		} else if (ARRAY_TYPE_PATTERN.matcher(type).matches()) {// 这里废弃了java里的String[]类型
			this.category = Constants.ARRAY_TYPE;
			this.type = "array";
			this.genericTypes.put("E", type.substring(0, type.indexOf("[")));

		} else if (GENERIC_TYPE_PATTERN.matcher(type).matches() && type.startsWith("map<")) {
			this.category = Constants.MAP_TYPE;
			this.type = "map";
			this.genericTypes.put("K", type.substring(type.indexOf("<") + 1, type.indexOf(",")));
			this.genericTypes.put("V", type.substring(type.indexOf(",") + 1, type.indexOf(">")));

		} else if (GENERIC_TYPE_PATTERN.matcher(type).matches()) {
			this.category = Constants.GENERIC_CLASS_TYPE;
			this.type = type.substring(0, type.indexOf("<"));
			this.genericTypes.put("K", type.substring(type.indexOf("<") + 1, type.indexOf(",")));
			this.genericTypes.put("V", type.substring(type.indexOf(",") + 1, type.indexOf(">")));

		} else {
			throw new RuntimeException("Type not currently supported!type:" + type);

		}

	}

	@Override
	public String getName() {
		return type;
	}

	@Override
	public boolean isBool() {
		return Constants.BOOL_TYPE.equals(category);
	}

	@Override
	public boolean isInt() {
		return Constants.INT_TYPE.equals(category);
	}

	@Override
	public boolean isLong() {
		return Constants.LONG_TYPE.equals(category);
	}

	@Override
	public boolean isDouble() {
		return Constants.DOUBLE_TYPE.equals(category);
	}

	@Override
	public boolean isStr() {
		return Constants.STR_TYPE.equals(category);
	}

	@Override
	public boolean isObj() {
		return Constants.OBJ_TYPE.equals(category);
	}

	@Override
	public boolean isVoid() {
		return Constants.VOID_TYPE.equals(category);
	}

	@Override
	public boolean isClass() {
		return Constants.BOOL_TYPE.equals(category);
	}

	@Override
	public boolean isArray() {
		return Constants.ARRAY_TYPE.equals(category);
	}

	@Override
	public boolean isMap() {
		return Constants.MAP_TYPE.equals(category);
	}

	@Override
	public boolean isGenericClass() {
		return Constants.GENERIC_CLASS_TYPE.equals(category);
	}

}
