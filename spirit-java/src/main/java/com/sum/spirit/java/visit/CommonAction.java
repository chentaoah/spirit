package com.sum.spirit.java.visit;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sum.spirit.core.visit.AbsElementAction;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.utils.Maps;
import com.sum.spirit.utils.StmtVisiter;

@Component
@Order(-100)
public class CommonAction extends AbsElementAction {

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element.statement;
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			if (currentToken.isArrayInit()) {// String[10] => new String[10]
				Statement subStatement = currentToken.getValue();
				subStatement.addToken(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (currentToken.isTypeInit()) {// User() => new User()
				Statement subStatement = currentToken.getValue();
				subStatement.addToken(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (currentToken.isList()) {// ["value"] => Lists.newArrayList("value");
				Statement subStatement = currentToken.getValue();
				subStatement.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Lists.newArrayList("));
				subStatement.setToken(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Lists.class.getName());

			} else if (currentToken.isMap()) {// {"key":"value"} => Maps.of("key","value");
				Statement subStatement = currentToken.getValue();
				for (Token subToken : subStatement.tokens) {
					if (subToken.isSeparator() && ":".equals(subToken.toString())) {
						subToken.value = ",";
					}
				}
				subStatement.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Maps.of("));
				subStatement.setToken(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Maps.class.getName());
			}
			return null;
		});
	}

}
