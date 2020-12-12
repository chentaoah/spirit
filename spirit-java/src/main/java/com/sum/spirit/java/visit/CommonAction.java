package com.sum.spirit.java.visit;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sum.spirit.core.visit.AbsElementAction;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.element.impl.Statement;
import com.sum.spirit.pojo.element.impl.Token;
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
		new StmtVisiter().visit(statement, visitEvent -> {
			Token token = visitEvent.item;
			if (token.isArrayInit()) {// String[10] => new String[10]
				Statement subStatement = token.getValue();
				subStatement.addToken(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (token.isTypeInit()) {// User() => new User()
				Statement subStatement = token.getValue();
				subStatement.addToken(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (token.isList()) {// ["value"] => Lists.newArrayList("value");
				Statement subStatement = token.getValue();
				subStatement.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Lists.newArrayList("));
				subStatement.setToken(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Lists.class.getName());

			} else if (token.isMap()) {// {"key":"value"} => Maps.of("key","value");
				Statement subStatement = token.getValue();
				for (Token subToken : subStatement.tokens) {
					if (subToken.isSeparator() && ":".equals(subToken.toString())) {
						subToken.value = ",";
					}
				}
				subStatement.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Maps.of("));
				subStatement.setToken(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Maps.class.getName());
			}
		});
	}

}
