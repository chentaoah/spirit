package com.sum.jass.core.deduce;

import com.sum.pisces.core.ProxyFactory;
import com.sum.jass.api.deduce.TypeDeclarer;
import com.sum.jass.api.link.TypeFactory;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.element.Element;
import com.sum.jass.pojo.element.Token;

public class TypeDeclarerImpl implements TypeDeclarer {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void declare(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			Token typeToken = element.getToken(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(factory.create(clazz, typeToken));

		} else if (element.isCatch()) {// }catch Exception e{
			Token typeToken = element.getToken(2);
			Token varToken = element.getToken(3);
			varToken.setTypeAtt(factory.create(clazz, typeToken));
		}
	}

}
