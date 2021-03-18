package com.sum.spirit.core.compile;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.constants.Constants;
import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.api.ElementAction;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IVariable;
import com.sum.spirit.core.compile.deduce.SimpleDeducer;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.compile.entity.MethodContext;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

@Component
@DependsOn("springUtils")
public class ElementVisiter implements InitializingBean {

	@Autowired
	public SimpleDeducer deducer;

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
					action.handle(event);
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
			return createVariable(element.get(0));

		} else if (element.isDeclare() || element.isDeclareAssign() || element.isForIn()) {
			return createVariable(element.get(1));

		} else if (element.isCatch()) {
			return createVariable(element.get(3));

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
