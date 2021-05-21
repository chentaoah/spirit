package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CommonResult;

public interface CharsHandler {

	CommonResult handle(CharsContext context, StringBuilder builder);

}
