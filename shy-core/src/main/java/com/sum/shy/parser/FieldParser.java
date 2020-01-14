package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.parser.api.Parser;

public class FieldParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 注解
		List<String> annotations = Context.get().getAnnotations();
		// 名称
		String name = stmt.get(1);
		// 字段
		IField field = new IField(annotations, scope, null, name, stmt);
		// 添加字段
		clazz.addField(field);

		return 0;
	}

}