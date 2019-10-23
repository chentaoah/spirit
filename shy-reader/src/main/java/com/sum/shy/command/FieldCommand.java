package com.sum.shy.command;

import java.util.List;
import java.util.regex.Pattern;

import com.sum.shy.clazz.Clazz;
import com.sum.shy.clazz.Field;
import com.sum.shy.sentence.Sentence;

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
	public int handle(String scope, Clazz clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			createField(clazz, clazz.staticFields, sentence);
		} else if ("class".equals(scope)) {
			createField(clazz, clazz.fields, sentence);
		}
		return 0;
	}

	private void createField(Clazz clazz, List<Field> fields, Sentence sentence) {

		// 变量名
		String name = sentence.getStr(0);
		// 类型
		String type = getType(sentence);
		// 尝试从上下文中获取
		if ("var".equals(type)) {
			type = clazz.defTypes.get(name);
		}
		// 这个field没有value,只有对应的语句,后面会去处理
		fields.add(new Field(type, name, sentence));

	}

	private String getType(Sentence sentence) {
		// 获取单元内容
		String str = sentence.getStr(2);
		if (BOOLEAN_PATTERN.matcher(str).matches()) {
			return "boolean";
		} else if (INT_PATTERN.matcher(str).matches()) {
			return "int";
		} else if (DOUBLE_PATTERN.matcher(str).matches()) {
			return "double";
		} else if (STR_PATTERN.matcher(str).matches()) {
			return "str";
		} else if (INVOKE_PATTERN.matcher(str).matches()) {
			return processInvoke(sentence, str);
		} else if (ARRAY_PATTERN.matcher(str).matches()) {
			return "array";
		} else if (MAP_PATTERN.matcher(str).matches()) {
			return "map";
		} else if (VAR_PATTERN.matcher(str).matches()) {// 变量
			return "var";
		}
		return null;
	}

	// 如果是构造函数,很容易知道类型
	private String processInvoke(Sentence sentence, String str) {

		return "var";
	}

}