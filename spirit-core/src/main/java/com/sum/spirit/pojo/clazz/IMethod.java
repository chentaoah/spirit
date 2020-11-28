package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.type.TypeFactory;
import com.sum.spirit.pojo.clazz.frame.IMember;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.utils.SpringUtils;

public class IMethod extends IMember {

	public List<IParameter> parameters = new ArrayList<>();

	public IMethod(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	public String getName() {
		Token methodToken = element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
		if (methodToken.isTypeInit()) {
			return methodToken.attr(AttributeEnum.SIMPLE_NAME);

		} else if (methodToken.isLocalMethod()) {
			return methodToken.attr(AttributeEnum.MEMBER_NAME);
		}
		throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
	}

	public boolean isInit() {
		Token methodToken = element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
		if (methodToken.isTypeInit()) {
			return true;

		} else if (methodToken.isLocalMethod()) {
			return false;
		}
		throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
	}

	public boolean isMatch(IType type, String methodName, List<IType> parameterTypes) {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		ClassLinker linker = SpringUtils.getBean(ClassLinker.class);
		if (getName().equals(methodName) && parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : parameters) {
				IType parameterType = factory.populate(type, parameter.getType());
				if (!linker.isMoreAbstract(parameterType, parameterTypes.get(count++))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
