package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CommonResult;

public interface CharAction {

	boolean isTrigger(CharEvent event);

	CommonResult handle(CharEvent event);

}
