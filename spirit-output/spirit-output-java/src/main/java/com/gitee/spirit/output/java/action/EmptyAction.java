package com.gitee.spirit.output.java.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.utils.StmtVisiter;
import com.gitee.spirit.stdlib.Emptys;

@Component
@Order(-100)
public class EmptyAction extends ExtElementAction {

	@Override
	public void visitElement(VisitContext context, Element element) {
		IClass clazz = context.clazz;
		StmtVisiter.visit(element, stmt -> {
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
