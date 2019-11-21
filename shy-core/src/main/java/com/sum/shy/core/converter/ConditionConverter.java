package com.sum.shy.core.converter;

import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class ConditionConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		
		return stmt;
	}

//	private String convertJudgeStmt(CtClass clazz, Stmt stmt) {
//		for (int i = 0; i < stmt.tokens.size(); i++) {
//			Token token = stmt.getToken(i);
//			if (token.isVar()) {
//				// 如果是str类型
////				if (token.getTypeAtt().isStr()) {
////					// 添加依赖
////					clazz.addImport(StringUtils.class.getName());
////					try {
////						Token nextToken = stmt.getToken(i + 1);
////						if (nextToken.isOperator() && "==".equals(nextToken.value)) {
////							Token paramToken = stmt.getToken(i + 2);
////							stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
////									"StringUtils.equals(" + token.value + "," + paramToken.value + ")", null));
////							stmt.tokens.remove(i + 2);
////							stmt.tokens.remove(i + 1);
////						} else if (nextToken.isOperator() && "!=".equals(nextToken.value)) {
////							Token paramToken = stmt.getToken(i + 2);
////							stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
////									"!StringUtils.equals(" + token.value + "," + paramToken.value + ")", null));
////							stmt.tokens.remove(i + 2);
////							stmt.tokens.remove(i + 1);
////						} else {
////							stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
////									"StringUtils.isNotEmpty(" + token.value + ")", null));
////						}
////					} catch (Exception e) {
////						// ignore
////					}
////				}
//			}
//		}
//		return stmt.toString();
//	}
}
