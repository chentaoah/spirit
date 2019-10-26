package com.sum.shy.command;

import java.util.List;

import com.sum.shy.clazz.Clazz;
import com.sum.shy.clazz.Field;
import com.sum.shy.sentence.Analyzer;
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
		// 类型
		String type = Analyzer.getType(clazz.defTypes, sentence);
		// 如果是集合类型
		List<String> genericTypes = Analyzer.getGenericTypes(clazz.defTypes, type, sentence);
		// 这个field没有value,只有对应的语句,后面会去处理
		fields.add(new Field(type, genericTypes, name, sentence));

	}

}