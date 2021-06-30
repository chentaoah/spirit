package com.gitee.spirit.core.compile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.api.ElementAction;
import com.gitee.spirit.core.api.ElementVisitor;
import com.gitee.spirit.core.api.StatementDeducer;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.action.AbstractAppElementAction;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

@Component
public class DefaultElementVisitor implements ElementVisitor {

	@Autowired
	public List<AbstractAppElementAction> actions;
	@Autowired
	public StatementDeducer deducer;

	@Override
	public IVariable visitElement(VisitContext context, Element element) {
		try {
			for (ElementAction action : actions) {
				action.visitElement(context, element);
			}
			return getVariableIfPossible(context, element);

		} catch (Exception e) {
			element.debug();
			throw new RuntimeException("Failed to derive element!", e);
		}
	}

	public IVariable getVariableIfPossible(VisitContext context, Element element) {
		if (element.isAssign()) {
			return createVariable(element.get(0));

		} else if (element.isDeclare() || element.isDeclareAssign() || element.isForIn()) {
			return createVariable(element.get(1));

		} else if (element.isCatch()) {
			return createVariable(element.get(3));

		} else if (element.isReturn()) {
			Statement statement = element.subStmt(1, element.size());
			IVariable variable = new IVariable(null);
			variable.setType(deducer.derive(statement));
			return variable;
		}
		return null;
	}

	public IVariable createVariable(Token varToken) {
		IVariable variable = new IVariable(varToken);
		variable.setType(varToken.attr(Attribute.TYPE));
		return variable;
	}

}
