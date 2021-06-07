package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.entity.VisitContext;

public interface VariableTracker {

	IType findVariableType(VisitContext context, String variableName);

}
