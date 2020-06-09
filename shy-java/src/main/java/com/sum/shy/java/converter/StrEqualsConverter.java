package com.sum.shy.java.converter;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.FastDeducer;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.common.Constants;
import com.sum.shy.common.StaticType;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.utils.TreeUtils;

public class StrEqualsConverter {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	public static void convertStmt(IClass clazz, Statement stmt) {
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
				Statement lastSubStmt = stmt.subStmt(start, i);
				IType lastType = deducer.derive(clazz, lastSubStmt);
				if (lastType.isStr()) {
					int end = TreeUtils.findEnd(stmt, i);
					Statement nextSubStmt = stmt.subStmt(i + 1, end);
					IType nextType = deducer.derive(clazz, nextSubStmt);
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
