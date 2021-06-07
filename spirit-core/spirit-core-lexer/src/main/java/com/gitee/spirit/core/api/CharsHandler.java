package com.gitee.spirit.core.api;

import com.gitee.spirit.core.lexer.entity.CharsContext;
import com.gitee.spirit.core.lexer.entity.CommonResult;

public interface CharsHandler {

	CommonResult handle(CharsContext context, StringBuilder builder);

}
