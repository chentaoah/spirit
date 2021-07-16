package com.gitee.spirit.core.api;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.core.lexer.entity.CharsContext;

public interface CharsHandler {

	Result handle(CharsContext context, StringBuilder builder);

}
