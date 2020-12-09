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
@Order(-80)
public class StrEqualsConverter extends AbsElementAction {

	public static final String FORMAT = "StringUtils.equals(%s, %s)";

	@Autowired
	public FastDeducer deducer;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.getStatement();
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			// 如果是==或者是!=
			if (currentToken.isEquals() || currentToken.isUnequals()) {
				int start = TreeUtils.findStartByTreeId(stmt, index);
				Statement lastStatement = stmt.subStmt(start, index);
				IType lastType = deducer.derive(clazz, lastStatement);
				if (TypeUtils.isStr(lastType)) {
					int end = TreeUtils.findEndByTreeId(stmt, index);
					Statement nextStatement = stmt.subStmt(index + 1, end);
					IType nextType = deducer.derive(clazz, nextStatement);
					if (TypeUtils.isStr(nextType)) {
						String text = String.format(currentToken.isEquals() ? FORMAT : "!" + FORMAT, lastStatement, nextStatement);
						Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
						expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
						expressToken.setAttr(AttributeEnum.TREE_ID, currentToken.attr(AttributeEnum.TREE_ID));
						stmt.replaceTokens(start, end, expressToken);
						clazz.addImport(StringUtils.class.getName());
					}
				}
			}
			return null;
		});
	}

}
