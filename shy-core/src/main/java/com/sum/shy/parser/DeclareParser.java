package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.api.Type;

public class DeclareParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 注解
		List<String> annotations = Context.get().getAnnotations();
		// 类型
		Type type = new CodeType(clazz, stmt.getToken(0));
		// 名称
		String name = stmt.get(1);
		// 字段
		IField field = new IField(annotations, scope, type, name, stmt);
		// 添加字段
		clazz.addField(field);

		return 0;
	}

}