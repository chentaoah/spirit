package com.gitee.spirit.core.clazz.entity;

import com.gitee.spirit.core.clazz.frame.TokenEntity;
import com.gitee.spirit.core.element.entity.Token;

public class IVariable extends TokenEntity {

    public String blockId;

    public IVariable(Token token) {
        super(token);
    }

    public String getName() {
        if (token == null) {
            return "NO_NAME";
        }
        if (token.isVariable()) {
            return token.toString();
        }
        throw new RuntimeException("The token is not a variable!");
    }

}
