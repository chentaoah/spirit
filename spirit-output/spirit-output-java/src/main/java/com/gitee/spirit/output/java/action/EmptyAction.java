package com.gitee.spirit.output.java.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.annotation.Native;
import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.compile.action.AbstractElementAction;
import com.gitee.spirit.core.compile.entity.ElementEvent;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.utils.StmtVisiter;
import com.gitee.spirit.stdlib.Emptys;

@Native
@Component
@Order(-100)
public class EmptyAction extends AbstractElementAction {

	@Override
	public void handle(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element;
		StmtVisiter.visit(statement, stmt -> {
			stmt.forEach(token -> {
				if (token.isLocalMethod()) {// empty(str)
					if (KeywordEnum.EMPTY.value.equals(token.attr(Attribute.MEMBER_NAME))) {
						clazz.addStaticImport(Emptys.class.getName() + ".empty");
					}
				}
			});
		});
	}

}
