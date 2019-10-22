package com.sum.shy.command;

import java.util.List;
import java.util.regex.Pattern;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SField;

public class FieldCommand extends AbstractCommand {

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\\$str[0-9]+$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^\\$invoke[0-9]+$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\$array[0-9]+$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\$map[0-9]+$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+$");

	@Override
	public int handle(String scope, SClass clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			createField(clazz.staticFields, sentence);
		} else if ("class".equals(scope)) {
			createField(clazz.fields, sentence);
		}
		return 0;
	}

	private void createField(List<SField> fields, Sentence sentence) {

		// 变量名
		String name = sentence.getUnit(0);
		// 类型
		String type = getType(sentence.getUnit(2));
		// 将所有的初始化的操作,放到构造函数里面去做,所以这里的字段的值都是null
		fields.add(new SField(type, name, null));

	}

	private String getType(String str) {

		if (BOOLEAN_PATTERN.matcher(str).matches()) {
			return "boolean";
		} else if (INT_PATTERN.matcher(str).matches()) {
			return "int";
		} else if (DOUBLE_PATTERN.matcher(str).matches()) {
			return "double";
		} else if (STR_PATTERN.matcher(str).matches()) {
			return "str";
		} else if (INVOKE_PATTERN.matcher(str).matches()) {
			return "var";
		} else if (ARRAY_PATTERN.matcher(str).matches()) {
			return "array";
		} else if (MAP_PATTERN.matcher(str).matches()) {
			return "map";
		} else if (VAR_PATTERN.matcher(str).matches()) {// 变量
			return "var";
		}
		return null;
	}

}