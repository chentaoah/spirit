package com.gitee.spirit.core.clazz.entity;

import java.util.ArrayList;
import java.util.List;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.clazz.frame.MemberEntity;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Token;
import com.google.common.base.Joiner;

public class IMethod extends MemberEntity {

    public List<IParameter> parameters = new ArrayList<>();

    public IMethod(List<IAnnotation> annotations, Element element) {
        super(annotations, element);
    }

    @Override
    public String getName() {
        Token methodToken = element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
        if (methodToken.isTypeInit()) {
            return methodToken.attr(Attribute.SIMPLE_NAME);

        } else if (methodToken.isLocalMethod()) {
            return methodToken.attr(Attribute.MEMBER_NAME);
        }
        throw new RuntimeException("The token is not a method!");
    }

    public boolean isInit() {
        Token methodToken = element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
        if (methodToken.isTypeInit()) {
            return true;

        } else if (methodToken.isLocalMethod()) {
            return false;
        }
        throw new RuntimeException("The token is not a method!");
    }

    @Override
    public String toString() {
        return getName() + "(" + Joiner.on(", ").join(parameters) + ")";
    }

    public String toSimpleString() {
        List<String> names = new ArrayList<>();
        parameters.forEach(parameter -> names.add(parameter.getName()));
        return getName() + "(" + Joiner.on(", ").join(names) + ")";
    }

}
