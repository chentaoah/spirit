package com.sum.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.annotation.App;
import com.sum.spirit.common.constants.Attribute;
import com.sum.spirit.core.api.TypeFactory;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Token;

@App
@Component
@Order(-100)
public class ElementTypeDeclarer extends AbstractScopeElementAction {

	@Autowired
	public TypeFactory factory;

	@Override
	public void handle(ElementEvent event) {
		Element element = event.element;
		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			setTypeByTypeToken(event.clazz, element.get(0), element.get(1));
		}
		super.handle(event);
	}

	@Override
	public void visitMethodScope(ElementEvent event) {
		Element element = event.element;
		if (element.isCatch()) {// } catch Exception e {
			setTypeByTypeToken(event.clazz, element.get(2), element.get(3));
		}
	}

	public void setTypeByTypeToken(IClass clazz, Token typeToken, Token varToken) {
		varToken.setAttr(Attribute.TYPE, factory.create(clazz, typeToken));
	}

}
