package com.gitee.spirit.core.clazz.entity;

import cn.hutool.core.lang.Assert;
import com.gitee.spirit.core.clazz.frame.NamedEntity;
import com.gitee.spirit.core.clazz.frame.TokenEntity;
import com.gitee.spirit.core.element.entity.Token;

public class IVariable extends TokenEntity implements NamedEntity {

    public String blockId;

    public IVariable(Token token) {
        super(token);
    }

    @Override
    public String getName() {
        Assert.isTrue(token.isVariable(), "The token is not a variable!");
        return token.toString();
    }

}
