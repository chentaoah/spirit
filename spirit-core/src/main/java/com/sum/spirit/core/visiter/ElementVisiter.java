package com.sum.spirit.core.visiter;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ElementAction;
import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.clazz.pojo.IVariable;
import com.sum.spirit.core.common.Constants;
import com.sum.spirit.core.common.enums.AttributeEnum;
import com.sum.spirit.core.element.pojo.Element;
import com.sum.spirit.core.element.pojo.Statement;
import com.sum.spirit.core.element.pojo.Token;
import com.sum.spirit.core.visiter.handler.FastDeducer;
import com.sum.spirit.core.visiter.pojo.ElementEvent;
import com.sum.spirit.core.visiter.pojo.MethodContext;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class ElementVisiter implements InitializingBean {

	@Autowired
	public FastDeducer deducer;

	public List<ElementAction> actions;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(ElementAction.class, Constants.SPIRIT_CORE_PACKAGE);
	}

	public IVariable visitElement(IClass clazz, Element element) {
		return visitElement(clazz, element, null);
	}

	public IVariable visitElement(IClass clazz, Element element, MethodContext context) {
		try {
			for (ElementAction action : actions) {
				ElementEvent event = new ElementEvent(clazz, element, context);
				if (action.isTrigger(event)) {
					action.visit(event);
				}
			}
			return getVariableIfPossible(clazz, element);

		} catch (Exception e) {
			element.debug();
			throw new RuntimeException("Failed to derive element!", e);
		}
	}

	public IVariable getVariableIfPossible(IClass clazz, Element element) {
		if (element.isAssign()) {
			return createVariable(element.getToken(0));

		} else if (element.isDeclare() || element.isDeclareAssign() || element.isForIn()) {
			return createVariable(element.getToken(1));

		} else if (element.isCatch()) {
			return createVariable(element.getToken(3));

		} else if (element.isReturn()) {
			Statement statement = element.subStmt(1, element.size());
			IVariable variable = new IVariable(null);
			variable.setType(deducer.derive(clazz, statement));
			return variable;
		}
		return null;
	}

	public IVariable createVariable(Token varToken) {
		IVariable variable = new IVariable(varToken);
		variable.setType(varToken.attr(AttributeEnum.TYPE));
		return variable;
	}

}
