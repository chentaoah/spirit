package com.gitee.spirit.core.clazz.entity;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.lang.Assert;
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

    public Token getMethodToken() {
        Token methodToken = element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
        Assert.notNull(methodToken, "The method token cannot be null!");
        return methodToken;
    }

    @Override
    public String getName() {
        Token methodToken = getMethodToken();
        return methodToken.attr(Attribute.SIMPLE_NAME, Attribute.MEMBER_NAME);
    }

    public boolean isInit() {
        Token methodToken = getMethodToken();
        return methodToken.isTypeInit();
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
