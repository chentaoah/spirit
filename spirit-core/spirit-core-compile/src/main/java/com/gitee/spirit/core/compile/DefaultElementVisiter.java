package com.gitee.spirit.core.compile;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.annotation.App;
import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.utils.SpringUtils;
import com.gitee.spirit.core.api.ElementAction;
import com.gitee.spirit.core.api.ElementVisiter;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.deduce.FragmentDeducer;
import com.gitee.spirit.core.compile.entity.ElementEvent;
import com.gitee.spirit.core.compile.entity.MethodContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

@Component
@DependsOn("springUtils")
public class DefaultElementVisiter implements ElementVisiter, InitializingBean {

	@Autowired
	public FragmentDeducer deducer;
	public List<ElementAction> actions;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansByAnnotation(ElementAction.class, App.class);
	}

	@Override
	public IVariable visitElement(IClass clazz, Element element) {
		return visitElement(clazz, null, element);
	}

	@Override
	public IVariable visitElement(IClass clazz, MethodContext context, Element element) {
		try {
			for (ElementAction action : actions) {
				ElementEvent event = new ElementEvent(clazz, context, element);
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
		variable.setType(varToken.attr(Attribute.TYPE));
		return variable;
	}

}
