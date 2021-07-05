package com.gitee.spirit.core.compile.entity;

import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.element.entity.Token;

public class NullVariable extends IVariable {

    public NullVariable() {
        super(null);
    }

    @Override
    public String getName() {
        return "NO_NAME";
    }

}
