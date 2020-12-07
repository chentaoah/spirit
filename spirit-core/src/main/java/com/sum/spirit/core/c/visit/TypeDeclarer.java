package com.sum.spirit.core.c.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.d.type.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

@Component
public class TypeDeclarer {

	@Autowired
	public TypeFactory factory;

	public void declare(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			Token typeToken = element.getToken(0);
			Token varToken = element.getToken(1);
			varToken.setAttr(AttributeEnum.TYPE, factory.create(clazz, typeToken));

		} else if (element.isCatch()) {// }catch Exception e{
			Token typeToken = element.getToken(2);
			Token varToken = element.getToken(3);
			varToken.setAttr(AttributeEnum.TYPE, factory.create(clazz, typeToken));
		}
	}

}
