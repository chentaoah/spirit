package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Stmt;

public class FieldParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<String> lines, int index, String line, Stmt stmt) {

		// 变量追踪
		VariableTracker.check(clazz, null, null, stmt);
		// 类型
		String type = TypeDerivator.getType(stmt);
		// 如果是集合类型,还要获取泛型
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
			clazz.staticFields.add(new Field(type, genericTypes, name, stmt));
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.fields.add(new Field(type, genericTypes, name, stmt));
		}

		return 0;
	}

}