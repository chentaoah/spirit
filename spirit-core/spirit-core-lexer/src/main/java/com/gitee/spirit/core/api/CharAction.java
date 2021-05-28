package com.gitee.spirit.core.api;

import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CommonResult;

public interface CharAction {

	boolean isTrigger(CharEvent event);

	CommonResult handle(CharEvent event);

}
