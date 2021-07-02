package com.gitee.spirit.core.clazz.entity;

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
        if (token.isAnnotation()) {
            return token.attr(Attribute.SIMPLE_NAME);
        }
        throw new RuntimeException("The token is not an annotation!");
    }
}
