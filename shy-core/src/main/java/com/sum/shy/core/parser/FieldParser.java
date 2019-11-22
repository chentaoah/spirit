package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class FieldParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 变量追踪
		VariableTracker.track(clazz, null, null, line, stmt);
		// 变量名
		String name = stmt.get(0);
		// 这里不再直接推导类型
		if (Constants.STATIC_SCOPE.equals(scope)) {
			clazz.addStaticField(new CtField(null, name, stmt));
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.addField(new CtField(null, name, stmt));
		}

		return 0;
	}

}