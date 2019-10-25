package com.sum.shy.command;

import java.util.List;

import com.sum.shy.clazz.Clazz;
import com.sum.shy.clazz.Field;
import com.sum.shy.sentence.Morpheme;
import com.sum.shy.sentence.Sentence;

public class FieldCommand extends AbstractCommand {

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
		String name = sentence.getUnit(0);
		// 右值
		String value = sentence.getUnit(2);
		// 类型
		String type = Morpheme.getType(clazz.defTypes, value);
		// 如果还是返回还是未知的,则通过名称来获取类型
		if ("var".equals(type)) {
			if (clazz.defTypes.containsKey(name)) {
				type = clazz.defTypes.get(name);
			}
		}
		// 如果是集合类型
		List<String> genericTypes = Morpheme.getGenericTypes(clazz.defTypes, type, value);

		// 这个field没有value,只有对应的语句,后面会去处理
		fields.add(new Field(type, genericTypes, name, sentence));

	}

}