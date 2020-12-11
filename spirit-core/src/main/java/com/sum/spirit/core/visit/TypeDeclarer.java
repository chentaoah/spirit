package com.sum.spirit.core.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.element.impl.Element;
import com.sum.spirit.pojo.element.impl.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

@Component
@Order(-100)
public class TypeDeclarer extends AbsElementAction {

	@Autowired
	public TypeFactory factory;

	@Override
	public void visit(ElementEvent event) {
		Element element = event.element;
		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			setTypeByTypeToken(event.clazz, element.getToken(0), element.getToken(1));
		}
		super.visit(event);
	}

	@Override
	public void visitMethodScope(ElementEvent event) {
		Element element = event.element;
		if (element.isCatch()) {// } catch Exception e {
			setTypeByTypeToken(event.clazz, element.getToken(2), element.getToken(3));
		}
	}

	public void setTypeByTypeToken(IClass clazz, Token typeToken, Token varToken) {
		varToken.setAttr(AttributeEnum.TYPE, factory.create(clazz, typeToken));
	}

}
