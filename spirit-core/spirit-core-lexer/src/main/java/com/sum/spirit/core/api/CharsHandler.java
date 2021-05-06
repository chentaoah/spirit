package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CharsResult;

public interface CharsHandler {

	CharsResult handle(CharsContext context, StringBuilder builder);

}
