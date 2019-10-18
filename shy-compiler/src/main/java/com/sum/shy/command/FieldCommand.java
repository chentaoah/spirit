package com.sum.shy.command;

import java.util.List;
import java.util.regex.Pattern;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SField;
import com.sum.shy.entity.SMethod;

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
	public int handle(String scope, SClass clazz, SMethod method, List<String> lines, int index, Sentence sentence) {
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
		// 这里的value就是sentence的引用,因为求value可能非常复杂
		// 所以这里只是保存一下语句,以后进行处理
		fields.add(new SField(type, name, sentence));

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