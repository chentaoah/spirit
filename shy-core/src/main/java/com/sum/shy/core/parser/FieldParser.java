package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class FieldParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 类型推导
		VariableTracker.check(clazz, null, null, line, stmt);
		String type = TypeDerivator.getType(stmt);
		List<String> genericTypes = TypeDerivator.getGenericTypes(stmt);
		// 变量名
		String name = stmt.get(0);

		// 如果是变量,或者是不知类型的,则去全局配置里面找
		if (Constants.UNKNOWN.equals(type)) {
			if (clazz.defTypes.containsKey(name)) {
				type = clazz.defTypes.get(name);
			}
		}

		if (Constants.STATIC_SCOPE.equals(scope)) {
			clazz.addStaticField(new Field(type, genericTypes, name, stmt));
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.addField(new Field(type, genericTypes, name, stmt));
		}

		return 0;
	}

}