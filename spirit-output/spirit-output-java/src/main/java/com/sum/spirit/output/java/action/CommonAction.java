package com.sum.spirit.output.java.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.annotation.Native;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.compile.action.AbstractElementAction;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.element.utils.StmtVisiter;
import com.sum.spirit.stdlib.Lists;
import com.sum.spirit.stdlib.Maps;

@Native
@Component
@Order(-80)
public class CommonAction extends AbstractElementAction {

	@Override
	public void handle(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element;
		StmtVisiter.visit(statement, stmt -> {
			stmt.forEach(token -> {
				if (token.isArrayInit()) {// String[10] => new String[10]
					Statement subStatement = token.getValue();
					subStatement.add(0, new Token(TokenTypeEnum.KEYWORD, "new"));

				} else if (token.isTypeInit()) {// User() => new User()
					Statement subStatement = token.getValue();
					subStatement.add(0, new Token(TokenTypeEnum.KEYWORD, "new"));

				} else if (token.isList()) {// ["value"] => Lists.of("value");
					Statement subStatement = token.getValue();
					subStatement.set(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Lists.of("));
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
		});
	}

}
