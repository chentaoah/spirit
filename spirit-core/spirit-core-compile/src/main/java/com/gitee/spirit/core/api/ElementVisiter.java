package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.entity.MethodContext;
import com.gitee.spirit.core.element.entity.Element;

public interface ElementVisiter {

	IVariable visitElement(IClass clazz, Element element);

	IVariable visitElement(IClass clazz, MethodContext context, Element element);

}
