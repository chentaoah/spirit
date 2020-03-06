package com.sum.shy.core.processor;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.type.CodeType;

public class TypeDeclarer {

	public static void declare(IClass clazz, Element element) {
		if (element.isDeclare() || element.isDeclareAssign()) {// String text
			Token typeToken = element.getToken(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(new CodeType(clazz, typeToken));

		} else if (element.isCatch()) {// }catch Exception e{
			Token typeToken = element.getToken(2);
			Token varToken = element.getToken(3);
			varToken.setTypeAtt(new CodeType(clazz, typeToken));
		}
	}

}
