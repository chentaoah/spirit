package com.sum.spirit.output.java.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.annotation.Native;
import com.sum.spirit.common.constants.Attribute;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.compile.action.AbstractElementAction;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.utils.StmtVisiter;
import com.sum.spirit.stdlib.Emptys;

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
