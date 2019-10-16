package com.sum.shy.command;

import java.util.List;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SField;
import com.sum.shy.entity.SMethod;
import com.sum.shy.entity.SVar;

public class VarCommand extends AbstractCommand {
	@Override
	public int handle(String scope, SClass clazz, SMethod method, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			createField(clazz.staticFields, sentence);
		} else if ("class".equals(scope)) {
			createField(clazz.fields, sentence);
		} else if ("method".equals(scope)) {
			createVar(method.params, sentence);
		}
		return 0;
	}

	private void createField(List<SField> fields, Sentence sentence) {

		// 变量名
		String name = sentence.units.get(0);
		// 类型
		String type = getType(sentence);

		fields.add(new SField(type, name, value));

	}

	private void createVar(List<SVar> params, Sentence sentence) {

		// 变量名
		String name = sentence.units.get(0);
		// 值
		String value = sentence.units.get(2);
		// 类型
		String type = getType(value);

		params.add(new SVar(type, name, value));

	}

	private String getType(Sentence sentence) {

		if ("true".equals(sentence) || "false".equals(sentence)) {
			// 布尔值
			return "boolean";

		} else if (sentence.startsWith("\"") && sentence.endsWith("\"")) {
			// 字符串
			return "str";

		} else if (isNumber(sentence)) {
			// 数字
			if (sentence.contains(".")) {
				return "double";
			} else {
				return "int";
			}

		} else if (isList(sentence)) {
			// 数组集合
			return "list";

		} else if (isMap(sentence)) {
			// 键值对集合
			return "map";

		} else if (isObjectInit(sentence)) {
			// 对象
			return getObjectType(sentence);

		} else if (isMethodInvoke(sentence)) {
			// 方法调用
			getObjectType(sentence);

		} else {
			throw new RuntimeException("The field type cannot be determined!value:" + sentence);
		}
		return null;

	}

	private static boolean isNumber(String str) {
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}

	private boolean isList(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isMap(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isObjectInit(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isMethodInvoke(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private String getObjectType(String value) {
		// TODO Auto-generated method stub
		return null;
	}

}