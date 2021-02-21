package com.sum.spirit.java.visiter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.utils.Maps;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.visiter.action.AbstractElementAction;
import com.sum.spirit.core.visiter.entity.ElementEvent;
import com.sum.spirit.core.visiter.utils.StmtVisiter;

@Component
@Order(-100)
public class CommonAction extends AbstractElementAction {

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element;
		new StmtVisiter().visitVoid(statement, visitEvent -> {
			Token token = visitEvent.item;
			if (token.isArrayInit()) {// String[10] => new String[10]
				Statement subStatement = token.getValue();
				subStatement.add(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (token.isTypeInit()) {// User() => new User()
				Statement subStatement = token.getValue();
				subStatement.add(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (token.isList()) {// ["value"] => Lists.newArrayList("value");
				Statement subStatement = token.getValue();
				subStatement.set(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Lists.newArrayList("));
				subStatement.set(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Lists.class.getName());

			} else if (token.isMap()) {// {"key":"value"} => Maps.of("key","value");
				Statement subStatement = token.getValue();
				for (Token subToken : subStatement) {
					if (subToken.isSeparator() && ":".equals(subToken.toString())) {
						subToken.value = ",";
					}
				}
				subStatement.set(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Maps.of("));
				subStatement.set(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Maps.class.getName());
			}
		});
	}

}
