package com.sum.spirit.java.core.visit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.FastDeducer;
import com.sum.spirit.core.c.visit.AbsElementAction;
import com.sum.spirit.java.utils.TreeUtils;
import com.sum.spirit.java.utils.TypeUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.StmtVisiter;

@Component
@Order(-60)
public class StrLogicAction extends AbsElementAction {

	public static final String FORMAT = "StringUtils.isNotEmpty(%s)";

	@Autowired
	public FastDeducer deducer;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.getStatement();
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			// 如果是逻辑判断符&&或||
			if (currentToken.isLogical()) {
				if (currentToken.isNegate()) {
					replaceNextString(clazz, stmt, index, currentToken);

				} else if (currentToken.isLogicAnd() || currentToken.isLogicOr()) {
					replacePrevString(clazz, stmt, index, currentToken);
					replaceNextString(clazz, stmt, index, currentToken);
				}
			}
			return null;
		});
	}

	public void replacePrevString(IClass clazz, Statement statement, int index, Token token) {
		int start = TreeUtils.findStart(statement, index);
		Statement prevStatement = statement.subStmt(start, index);
		IType prevType = deducer.derive(clazz, prevStatement);
		if (TypeUtils.isString(prevType)) {
			String text = String.format(FORMAT, prevStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
			expressToken.setAttr(AttributeEnum.TREE_ID, token.attr(AttributeEnum.TREE_ID) + "-0");
			statement.replaceTokens(start, index, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

	public void replaceNextString(IClass clazz, Statement statement, int index, Token token) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType nextType = deducer.derive(clazz, nextStatement);
		if (TypeUtils.isString(nextType)) {
			String text = String.format(FORMAT, nextStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
			expressToken.setAttr(AttributeEnum.TREE_ID, token.attr(AttributeEnum.TREE_ID) + "-1");
			statement.replaceTokens(index + 1, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}
