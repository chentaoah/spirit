package com.sum.shy.core.command;

import java.util.List;
import java.util.Map;

import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Result;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class FieldCommand implements Command {

	@Override
	public Result analysis(String line, String syntax, List<String> words) {
		// 变量名
		String name = words.get(0);
		// 将所有单元转换成带有类型的token
		List<Token> tokens = SemanticDelegate.getTokens(words);
		// 生成语句,以便后面使用
		Stmt stmt = new Stmt(line, syntax, tokens);
		// 类型
		String type = SemanticDelegate.getTypeByStmt(stmt);
		// 如果是集合类型,还要获取泛型
		List<String> genericTypes = SemanticDelegate.getGenericTypes(stmt);

		// 如果是变量,或者是不知类型的,则去全局配置里面找
		if ("var".equals(type) || "unknown".equals(type)) {
			Map<String, String> defTypes = Context.get().clazz.defTypes;
			if (defTypes.containsKey(name)) {
				type = defTypes.get(name);
			}
		}

		Context context = Context.get();
		if ("static".equals(context.scope)) {
			context.clazz.staticFields.add(new Field(type, genericTypes, name, stmt));
		} else if ("class".equals(context.scope)) {
			context.clazz.fields.add(new Field(type, genericTypes, name, stmt));
		}

		return new Result(0, stmt);
	}

}