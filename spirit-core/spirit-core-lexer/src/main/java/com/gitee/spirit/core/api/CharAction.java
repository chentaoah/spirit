package com.gitee.spirit.core.api;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.core.lexer.entity.CharEvent;

public interface CharAction {

	boolean isTrigger(CharEvent event);

	Result handle(CharEvent event);

}
