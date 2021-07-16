package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.element.entity.Statement;

public interface StatementDeducer {

	IType derive(Statement statement);

}
