package com.gitee.spirit.core.clazz.entity;

import cn.hutool.core.lang.Assert;
import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.clazz.frame.NamedEntity;
import com.gitee.spirit.core.clazz.frame.TokenEntity;
import com.gitee.spirit.core.element.entity.Token;

public class IAnnotation extends TokenEntity implements NamedEntity {

    public IAnnotation(Token token) {
        super(token);
    }

    @Override
    public String getName() {
        Assert.isTrue(token.isAnnotation(), "The token is not an annotation!");
        return token.attr(Attribute.SIMPLE_NAME);
    }
}
