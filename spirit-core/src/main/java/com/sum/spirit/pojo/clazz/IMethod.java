package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;

public class IMethod extends AbsMember {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	public boolean isInit;

	public List<IParameter> parameters = new ArrayList<>();

	public IMethod(List<IAnnotation> annotations, Element element) {

		super(annotations, element);

		Token methodToken = element.findToken(Constants.TYPE_INIT_TOKEN, Constants.LOCAL_METHOD_TOKEN);
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
