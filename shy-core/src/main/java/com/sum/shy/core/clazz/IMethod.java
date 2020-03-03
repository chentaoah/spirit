package com.sum.shy.core.clazz;

import java.util.List;

import com.sum.shy.core.doc.Element;
import com.sum.shy.core.type.api.IType;

public class IMethod extends AbsMember {

	public IMethod(List<Element> annotations, boolean isStatic, Element element) {
		super(annotations, isStatic, element);
		// TODO 在这里解析method方法名，和参数信息
	}

	public List<IParameter> getParameters() {
		return null;
	}

	public boolean isMatch(String name, List<IType> paramTypes) {
		// TODO Auto-generated method stub
		return false;
	}

}
