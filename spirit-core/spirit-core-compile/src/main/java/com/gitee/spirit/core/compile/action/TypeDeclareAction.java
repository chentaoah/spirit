package com.gitee.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Token;

@Component
@Order(-100)
public class TypeDeclareAction extends AppElementAction {

	@Autowired
	public TypeFactory factory;

	@Override
	public void visitElement(VisitContext context, Element element) {
		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			setTypeByTypeToken(context.clazz, element.get(0), element.get(1));
		}
		super.visitElement(context, element);
	}

	@Override
	public void visitMethodScope(VisitContext context, Element element) {
		if (element.isCatch()) {// } catch Exception e {
			setTypeByTypeToken(context.clazz, element.get(2), element.get(3));
		}
	}

	public void setTypeByTypeToken(IClass clazz, Token typeToken, Token varToken) {
		varToken.setAttr(Attribute.TYPE, factory.create(clazz, typeToken));
	}

}
