package com.sum.shy.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.pojo.element.Token;

public class IMethod extends AbsMember {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	public boolean isInit;

	public boolean isSync;

	public List<IParameter> parameters = new ArrayList<>();

	public IMethod(List<IAnnotation> annotations, boolean isStatic, Element element) {

		super(annotations, isStatic, element);

		Token methodToken = element.findToken(Constants.TYPE_INIT_TOKEN, Constants.LOCAL_METHOD_TOKEN);
		if (methodToken.isTypeInit()) {
			isInit = true;
			isSync = false;
			name = methodToken.getSimpleName();

		} else if (methodToken.isLocalMethod()) {
			isInit = false;
			isSync = element.containsKeyword(Constants.SYNC_KEYWORD);
			name = methodToken.getMemberName();

		} else {
			throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
		}

	}

	public boolean isMatch(IType type, String methodName, List<IType> parameterTypes) {

		if (name.equals(methodName) && parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : parameters) {
				IType returnType = factory.populateType(type, parameter.type);
				if (!returnType.isMatch(parameterTypes.get(count++)))
					return false;
			}
			return true;
		}
		return false;
	}

}
