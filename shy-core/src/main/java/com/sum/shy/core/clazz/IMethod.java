package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;

public class IMethod extends AbsMember {

	public boolean isInit;

	public boolean isSync;

	public List<IParameter> parameters = new ArrayList<>();

	/**
	 * 构造方法，这个时候，还不知道方法的返回类型是什么
	 * 
	 * @param annotations
	 * @param isStatic
	 * @param element
	 */
	public IMethod(List<IAnnotation> annotations, boolean isStatic, Element element) {
		super(annotations, isStatic, element);
		// 方法可能本地方法，也可能是构造方法
		Token methodToken = element.findToken(Constants.LOCAL_METHOD_TOKEN);
		if (methodToken == null) {
			methodToken = element.findToken(Constants.TYPE_INIT_TOKEN);
			isInit = true;
			isSync = false;
			name = methodToken.getSimpleNameAtt();
		} else {
			isInit = false;
			isSync = element.containsKeyword(Constants.SYNC_KEYWORD);
			name = methodToken.getMemberNameAtt();
		}

	}

	public boolean isMatch(String methodName, List<IType> parameterTypes) {
		if (name.equals(methodName) && parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : parameters) {
				if (!parameter.type.equals(parameterTypes.get(count++)))
					return false;
			}
			return true;
		}
		return false;
	}

}
