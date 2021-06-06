package com.gitee.spirit.output.java.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.StmtVisiter;
import com.gitee.spirit.stdlib.Lists;
import com.gitee.spirit.stdlib.Maps;

@Component
@Order(-80)
public class CommonAction extends AbstractExtElementAction {

	@Override
	public void visitElement(VisitContext context, Element element) {
		IClass clazz = context.clazz;
		StmtVisiter.visit(element, stmt -> {
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
