package com.sum.spirit.java.core.visit;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.common.enums.TokenTypeEnum;
import com.sum.spirit.core.element.pojo.Statement;
import com.sum.spirit.core.element.pojo.Token;
import com.sum.spirit.core.visiter.action.AbstractElementAction;
import com.sum.spirit.core.visiter.pojo.ElementEvent;
import com.sum.spirit.core.visiter.utils.StmtVisiter;
import com.sum.spirit.utils.Maps;

@Component
@Order(-100)
public class CommonAction extends AbstractElementAction {

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element.statement;
		new StmtVisiter().visitVoid(statement, visitEvent -> {
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
