package com.sum.shy.command;

import java.util.List;

import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SField;
import com.sum.shy.entity.SMethod;
import com.sum.shy.entity.SVar;

public class VarCommand extends AbstractCommand {
	@Override
	public int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			createField(clazz.staticFields, line);
		} else if ("class".equals(scope)) {
			createField(clazz.fields, line);
		} else if ("method".equals(scope)) {
			createVar(method.params, line);
		}
		return 0;
	}

	private void createField(List<SField> fields, String line) {
		// 是否包含"="号
		if (line.contains("=")) {
			// 变量名
			String name = line.substring(0, line.indexOf("=")).trim();
			// 值
			String value = line.substring(line.indexOf("=") + 1).trim();
			// 类型
			String type = getType(line, value);

			fields.add(new SField(type, name, value));

		}

	}

	private void createVar(List<SVar> params, String line) {
		// 是否包含"="号
		if (line.contains("=")) {
			// 变量名
			String name = line.substring(0, line.indexOf("=")).trim();
			// 值
			String value = line.substring(line.indexOf("=") + 1).trim();
			// 类型
			String type = getType(line, value);

			params.add(new SVar(type, name, value));

		}
	}

	private String getType(String line, String value) {

		if ("true".equals(value) || "false".equals(value)) {
			// 布尔值
			return "boolean";

		} else if (value.startsWith("\"") && value.endsWith("\"")) {
			// 字符串
			return "str";

		} else if (isNumber(value)) {
			// 数字
			if (value.contains(".")) {
				return "double";
			} else {
				return "int";
			}

		} else if (isList(value)) {
			// 数组集合
			return "list";

		} else if (isMap(value)) {
			// 键值对集合
			return "map";

		} else if (isObjectInit(value)) {
			// 对象
			return getObjectType(value);

		} else if (isMethodInvoke(value)) {
			// 方法调用
			getObjectType(value);

		} else {
			throw new RuntimeException("The field type cannot be determined!line:" + line);
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