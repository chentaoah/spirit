package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.stmt.Element;
import com.sum.shy.core.stmt.Token;

public class IMethod extends AbsMember {

	public boolean isInit;

	public boolean isSync;

	public List<IParameter> parameters = new ArrayList<>();

	public IMethod(List<IAnnotation> annotations, boolean isStatic, Element element) {

		super(annotations, isStatic, element);

		Token methodToken = element.findToken(Constants.TYPE_INIT_TOKEN, Constants.LOCAL_METHOD_TOKEN);
		if (methodToken.isTypeInit()) {
			isInit = true;
			isSync = false;
			name = methodToken.getSimpleNameAtt();

		} else if (methodToken.isLocalMethod()) {
			isInit = false;
			isSync = element.containsKeyword(Constants.SYNC_KEYWORD);
			name = methodToken.getMemberNameAtt();

		} else {
			throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
		}

	}

	public boolean isMatch(String methodName, List<IType> parameterTypes) {
		if (name.equals(methodName) && parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : parameters) {
				if (!parameter.type.isMatch(parameterTypes.get(count++)))
					return false;
			}
			return true;
		}
		return false;
	}

}
