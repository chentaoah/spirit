package com.sum.shy.java.converter;

import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.clazz.pojo.IType;
import com.sum.shy.core.pojo.Constants;
import com.sum.shy.core.pojo.StaticType;
import com.sum.shy.document.pojo.Stmt;
import com.sum.shy.document.pojo.Token;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.member.processor.FastDeducer;
import com.sum.shy.utils.TreeUtils;

public class StrEqualsConverter {

	public static void convertStmt(IClass clazz, Stmt stmt) {
		// 如果有子节点，先处理子节点
		for (Token token : stmt.tokens) {
			if (token.canVisit())
				convertStmt(clazz, token.getStmt());
		}

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isOperator() && ("==".equals(token.toString()) || "!=".equals(token.toString()))) {
				// 向左遍历获取自己的分支
				int start = TreeUtils.findStart(stmt, i);
				// 截取出这一部分
				Stmt lastSubStmt = stmt.subStmt(start, i);
				IType lastType = FastDeducer.deriveStmt(clazz, lastSubStmt);
				if (lastType.isStr()) {
					int end = TreeUtils.findEnd(stmt, i);
					Stmt nextSubStmt = stmt.subStmt(i + 1, end);
					IType nextType = FastDeducer.deriveStmt(clazz, nextSubStmt);
					if (nextType.isStr()) {
						String format = null;
						if ("==".equals(token.toString())) {
							format = "StringUtils.equals(%s, %s)";
						} else if ("!=".equals(token.toString())) {
							format = "!StringUtils.equals(%s, %s)";
						}
						String text = String.format(format, lastSubStmt, nextSubStmt);
						Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
						expressToken.setTypeAtt(StaticType.BOOLEAN_TYPE);
						expressToken.setTreeId(token.getTreeId());
						stmt.replace(start, end, expressToken);
						clazz.addImport(StringUtils.class.getName());
					}
				}
			}
		}
	}

}
