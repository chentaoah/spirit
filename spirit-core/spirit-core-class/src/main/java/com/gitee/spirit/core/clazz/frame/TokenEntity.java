package com.gitee.spirit.core.clazz.frame;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.element.entity.Token;

public abstract class TokenEntity extends TypeEntity {

    public Token token;

    public TokenEntity(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token.toString();
    }

}
