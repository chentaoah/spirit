package com.sum.shy.java.converter;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.utils.TreeUtils;
import com.sum.shy.lib.StringUtils;

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
				IType type = FastDeducer.deriveStmt(clazz, lastSubStmt);
				if (type.isStr()) {
					int end = TreeUtils.findEnd(stmt, i);
					Stmt nextSubStmt = stmt.subStmt(i + 1, end);
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
