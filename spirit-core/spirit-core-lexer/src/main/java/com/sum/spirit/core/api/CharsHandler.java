package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharsContext;

public interface CharsHandler {

	void handle(CharsContext context, StringBuilder builder);

}
