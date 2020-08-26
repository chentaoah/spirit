package com.sum.spirit.java.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.convert.ElementConverter;
import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.TreeUtils;

@Component
@Order(-80)
public class StrEqualsConverter implements ElementConverter {

	@Autowired
	public FastDeducer deducer;

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.statement);
	}

	public void convertStmt(IClass clazz, Statement statement) {
		// Process the child nodes first, or it will affect the transformation of the
		// upper layer
		for (Token token : statement.tokens) {
			if (token.canSplit())
				convertStmt(clazz, token.getValue());
		}

		for (int index = 0; index < statement.size(); index++) {
			Token token = statement.getToken(index);
			if (token.isOperator() && ("==".equals(token.toString()) || "!=".equals(token.toString()))) {

				int start = TreeUtils.findStart(statement, index);
				Statement lastStatement = statement.subStmt(start, index);
				IType lastType = deducer.derive(clazz, lastStatement);
				if (lastType.isStr()) {

					int end = TreeUtils.findEnd(statement, index);
					Statement nextStatement = statement.subStmt(index + 1, end);
					IType nextType = deducer.derive(clazz, nextStatement);
					if (nextType.isStr()) {

						String format = null;
						if ("==".equals(token.toString())) {
							format = "StringUtils.equals(%s, %s)";
						} else if ("!=".equals(token.toString())) {
							format = "!StringUtils.equals(%s, %s)";
						}

						String text = String.format(format, lastStatement, nextStatement);
						Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
						expressToken.setTypeAtt(TypeTable.BOOLEAN_TYPE);
						expressToken.setTreeId(token.getTreeId());
						statement.replace(start, end, expressToken);
						clazz.addImport(StringUtils.class.getName());
					}
				}
			}
		}
	}

}
