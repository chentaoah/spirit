package com.sum.shy.java.convert;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.lib.StringUtils;

public class ConditionConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 如果是变量且是字符串类型
			if (token.isVar() && token.getTypeAtt().isStr()) {
				try {
					Token nextToken = stmt.getToken(i + 1);
					// 如果紧跟着==操作符
					if (nextToken.isOperator() && "==".equals(nextToken.value)) {
						Token paramToken = stmt.getToken(i + 2);
						stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
								"StringUtils.equals(" + token.value + ", " + paramToken.value + ")", null));
						// 移除原来的
						stmt.tokens.remove(i + 1);
						stmt.tokens.remove(i + 1);
						// 添加依赖
						clazz.addImport(StringUtils.class.getName());

					} else if (nextToken.isOperator() && "!=".equals(nextToken.value)) {
						// 如果紧跟着!=操作符
						Token paramToken = stmt.getToken(i + 2);
						stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
								"!StringUtils.equals(" + token.value + ", " + paramToken.value + ")", null));
						// 移除原来的
						stmt.tokens.remove(i + 1);
						stmt.tokens.remove(i + 1);
						// 添加依赖
						clazz.addImport(StringUtils.class.getName());

					} else {
						// 只有一个孤零零的字符串,则说明是判空
						stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
								"StringUtils.isNotEmpty(" + token.value + ")", null));
						// 添加依赖
						clazz.addImport(StringUtils.class.getName());
					}

				} catch (Exception e) {
					// ignore
				}
			}
		}
		stmt.tokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "(", null));
		stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));

		return stmt;
	}

}
