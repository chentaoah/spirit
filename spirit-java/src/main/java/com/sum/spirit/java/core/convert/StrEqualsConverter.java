package com.sum.spirit.java.core.convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.FastDeducer;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.java.utils.TreeUtils;
import com.sum.spirit.java.utils.TypeUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

@Component
@Order(-80)
public class StrEqualsConverter implements ElementConverter {

	public static final String FORMAT = "StringUtils.equals(%s, %s)";

	@Autowired
	public FastDeducer deducer;

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.statement);
	}

	public void convertStmt(IClass clazz, Statement statement) {
		// 先处理子节点，下层节点的结果，会间接影响上层
		for (Token token : statement.tokens) {
			if (token.canSplit()) {
				convertStmt(clazz, token.getValue());
			}
		}
		for (int index = 0; index < statement.size(); index++) {
			Token token = statement.getToken(index);
			if (token.isEquals() || token.isUnequals()) {
				int start = TreeUtils.findStartByTreeId(statement, index);
				Statement lastStatement = statement.subStmt(start, index);
				IType lastType = deducer.derive(clazz, lastStatement);
				if (TypeUtils.isStr(lastType)) {
					int end = TreeUtils.findEndByTreeId(statement, index);
					Statement nextStatement = statement.subStmt(index + 1, end);
					IType nextType = deducer.derive(clazz, nextStatement);
					if (TypeUtils.isStr(nextType)) {
						String text = String.format(token.isEquals() ? FORMAT : "!" + FORMAT, lastStatement, nextStatement);
						Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
						expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
						expressToken.setAttr(AttributeEnum.TREE_ID, token.attr(AttributeEnum.TREE_ID));
						statement.replaceTokens(start, end, expressToken);
						clazz.addImport(StringUtils.class.getName());
					}
				}
			}
		}
	}

}
