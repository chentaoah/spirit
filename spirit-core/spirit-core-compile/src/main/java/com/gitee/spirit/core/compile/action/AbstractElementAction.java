package com.gitee.spirit.core.compile.action;

import com.gitee.spirit.core.api.ElementAction;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;

public abstract class AbstractElementAction implements ElementAction {

	@Override
	public void visitElement(VisitContext context, Element element) {
		if (context.isFieldScope()) {
			visitFieldScope(context, element);

		} else if (context.isMethodScope()) {
			visitMethodScope(context, element);
		}
	}

	public void visitFieldScope(VisitContext context, Element element) {
		// ignore
	}

	public void visitMethodScope(VisitContext context, Element element) {
		// ignore
	}

}
