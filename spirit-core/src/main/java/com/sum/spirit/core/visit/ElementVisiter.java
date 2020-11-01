package com.sum.spirit.core.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

@Component
public class ElementVisiter {

	@Autowired
	public TypeDeclarer declarer;
	@Autowired
	public ExpressDeclarer expressDeclarer;
	@Autowired
	public VariableTracker tracker;
	@Autowired
	public InvokeVisiter visiter;
	@Autowired
	public FastDeducer deducer;

	public IVariable visit(IClass clazz, MethodContext context, Element element) {
		try {
			// 1.some statements need to declare variable types in advance
			declarer.declare(clazz, element);

			// 2.some statements need to deduce the type of a token based on an expression
			expressDeclarer.declare(clazz, context, element);

			// 3.get the type of variable from context
			tracker.track(clazz, context, element.statement);

			// 4.get the return value type of the method call
			visiter.visit(clazz, element.statement);

			// 5.determine whether the syntax declares a variable
			return getVariableIfPossible(clazz, element);

		} catch (Exception e) {
			element.debug();
			throw new RuntimeException("Failed to derive element!", e);
		}

	}

	public IVariable getVariableIfPossible(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign()) {
			Token varToken = element.getToken(1);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.getAttribute(AttributeEnum.TYPE));
			return variable;

		} else if (element.isCatch()) {
			Token varToken = element.getToken(3);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.getAttribute(AttributeEnum.TYPE));
			return variable;

		} else if (element.isAssign()) {
			Token varToken = element.getToken(0);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.getAttribute(AttributeEnum.TYPE));
			return variable;

		} else if (element.isForIn()) {
			Token varToken = element.getToken(1);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.getAttribute(AttributeEnum.TYPE));
			return variable;

		} else if (element.isReturn()) {
			Statement statement = element.subStmt(1, element.size());
			IVariable variable = new IVariable(null);
			variable.setType(deducer.derive(clazz, statement));
			return variable;
		}
		return null;
	}

}
