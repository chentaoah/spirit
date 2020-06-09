package com.sum.shy.processor;

import com.sum.shy.clazz.IClass;
import com.sum.shy.deducer.TypeFactory;
import com.sum.shy.element.Element;
import com.sum.shy.element.Token;

public class TypeDeclarer {

	public static void declare(IClass clazz, Element element) {
		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			Token typeToken = element.getToken(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(TypeFactory.create(clazz, typeToken));

		} else if (element.isCatch()) {// }catch Exception e{
			Token typeToken = element.getToken(2);
			Token varToken = element.getToken(3);
			varToken.setTypeAtt(TypeFactory.create(clazz, typeToken));
		}
	}

}
