package com.sum.shy.core.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.regex.Pattern;

import com.sum.shy.core.api.Type;

public class CodeType implements Type {

//	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9_]+$");
//	public static final Pattern ARRAY_TYPE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+\\[\\]$");
//	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+<[a-zA-Z0-9_<>,]+>$");

	public String className;// 完整的类名
	public Map<String, CodeType> genericTypes = new LinkedHashMap<>();// 泛型参数
	public String name;// 变量名
	public List<String> varNames;// 直接访问的属性名
	public String methodName;// 方法名
	public CodeType next;// 链表

	public CodeType(String className, Map<String, CodeType> genericTypes, String name, List<String> varNames,
			String methodName, CodeType next) {
		this.className = className;
		this.genericTypes = genericTypes == null ? new LinkedHashMap<>() : genericTypes;
		this.name = name;
		this.varNames = varNames;
		this.methodName = methodName;
		this.next = next;
	}

	@Override
	public Type next(Type type) {
		this.next = (CodeType) type;
		return type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(className);
		if (genericTypes.size() > 0) {
			sb.append("<");
			for (CodeType type : genericTypes.values()) {
				sb.append(type.toString() + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(">");
		}
		return sb.toString();
	}

//	public CodeType(String type) {// 这里也不再支持多层嵌套的泛型了
//
//		if ("bool".equals(type)) {
//			this.category = Constants.BOOL_TYPE;
//			this.name = "bool";
//
//		} else if ("int".equals(type)) {
//			this.category = Constants.INT_TYPE;
//			this.name = "int";
//
//		} else if ("long".equals(type)) {
//			this.category = Constants.LONG_TYPE;
//			this.name = "long";
//
//		} else if ("double".equals(type)) {
//			this.category = Constants.DOUBLE_TYPE;
//			this.name = "double";
//
//		} else if ("str".equals(type)) {
//			this.category = Constants.STR_TYPE;
//			this.name = "str";
//
//		} else if ("obj".equals(type)) {
//			this.category = Constants.OBJ_TYPE;
//			this.name = "obj";
//
//		} else if ("void".equals(type)) {
//			this.category = Constants.VOID_TYPE;
//			this.name = "void";
//
//		} else if (TYPE_PATTERN.matcher(type).matches()) {
//			this.category = Constants.CLASS_TYPE;
//			this.name = type;
//
//		} else if (ARRAY_TYPE_PATTERN.matcher(type).matches()) {// 这里废弃了java里的String[]类型
//			this.category = Constants.ARRAY_TYPE;
//			this.name = "array";
//			this.genericTypes.put("E", type.substring(0, type.indexOf("[")));
//
//		} else if (GENERIC_TYPE_PATTERN.matcher(type).matches() && type.startsWith("map<")) {
//			this.category = Constants.MAP_TYPE;
//			this.name = "map";
//			this.genericTypes.put("K", type.substring(type.indexOf("<") + 1, type.indexOf(",")));
//			this.genericTypes.put("V", type.substring(type.indexOf(",") + 1, type.indexOf(">")));
//
//		} else if (GENERIC_TYPE_PATTERN.matcher(type).matches()) {
//			this.category = Constants.GENERIC_CLASS_TYPE;
//			this.name = type.substring(0, type.indexOf("<"));
//			this.genericTypes.put("K", type.substring(type.indexOf("<") + 1, type.indexOf(",")));
//			this.genericTypes.put("V", type.substring(type.indexOf(",") + 1, type.indexOf(">")));
//
//		} else {
//			throw new RuntimeException("Type not currently supported!name:" + type);
//
//		}
//
//	}

//	@Override
//	public String getName() {
//		return name;
//	}
//
//	@Override
//	public boolean isBool() {
//		return Constants.BOOL_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isInt() {
//		return Constants.INT_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isLong() {
//		return Constants.LONG_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isDouble() {
//		return Constants.DOUBLE_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isStr() {
//		return Constants.STR_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isObj() {
//		return Constants.OBJ_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isVoid() {
//		return Constants.VOID_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isClass() {
//		return Constants.BOOL_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isArray() {
//		return Constants.ARRAY_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isMap() {
//		return Constants.MAP_TYPE.equals(category);
//	}
//
//	@Override
//	public boolean isGenericClass() {
//		return Constants.GENERIC_CLASS_TYPE.equals(category);
//	}

}
