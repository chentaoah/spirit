package com.sum.soon.java.convert;

import com.sum.pisces.api.annotation.Order;
import com.sum.pisces.core.ProxyFactory;
import com.sum.soon.api.convert.ElementConverter;
import com.sum.soon.api.deduce.FastDeducer;
import com.sum.soon.lib.StringUtils;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.common.Constants;
import com.sum.soon.pojo.common.TypeTable;
import com.sum.soon.pojo.element.Element;
import com.sum.soon.pojo.element.Statement;
import com.sum.soon.pojo.element.Token;
import com.sum.soon.utils.TreeUtils;

@Order(-80)
public class StrEqualsConverter implements ElementConverter {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.stmt);
	}

	public void convertStmt(IClass clazz, Statement stmt) {
		// Process the child nodes first, or it will affect the transformation of the
		// upper layer
		for (Token token : stmt.tokens) {
			if (token.canSplit())
				convertStmt(clazz, token.getValue());
		}

		for (int index = 0; index < stmt.size(); index++) {
			Token token = stmt.getToken(index);
			if (token.isOperator() && ("==".equals(token.toString()) || "!=".equals(token.toString()))) {

				int start = TreeUtils.findStart(stmt, index);
				Statement lastSubStmt = stmt.subStmt(start, index);
				IType lastType = deducer.derive(clazz, lastSubStmt);
				if (lastType.isStr()) {

					int end = TreeUtils.findEnd(stmt, index);
					Statement nextSubStmt = stmt.subStmt(index + 1, end);
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
						expressToken.setTypeAtt(TypeTable.BOOLEAN_TYPE);
						expressToken.setTreeId(token.getTreeId());
						stmt.replace(start, end, expressToken);
						clazz.addImport(StringUtils.class.getName());
					}
				}
			}
		}
	}

}
