package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenEnum;
import com.sum.spirit.utils.SpringUtils;

public class IMethod extends AbsMember {

	public boolean isInit;

	public List<IParameter> parameters = new ArrayList<>();

	public IMethod(List<IAnnotation> annotations, Element element) {

		super(annotations, element);

		Token methodToken = element.findToken(TokenEnum.TYPE_INIT, TokenEnum.LOCAL_METHOD);
		if (methodToken.isTypeInit()) {
			isInit = true;
			name = methodToken.getSimpleName();

		} else if (methodToken.isLocalMethod()) {
			isInit = false;
			name = methodToken.getMemberName();

		} else {
			throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
		}
	}

	public boolean isMatch(IType type, String methodName, List<IType> parameterTypes) {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		if (name.equals(methodName) && parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : parameters) {
				IType returnType = factory.populate(type, parameter.type);
				if (!returnType.isMatch(parameterTypes.get(count++)))
					return false;
			}
			return true;
		}
		return false;
	}

}
