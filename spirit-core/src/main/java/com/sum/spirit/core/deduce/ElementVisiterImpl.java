package com.sum.spirit.core.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.PostProcessor;
import com.sum.spirit.api.deduce.ElementVisiter;
import com.sum.spirit.api.deduce.ExpressDeclarer;
import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.api.deduce.InvokeVisiter;
import com.sum.spirit.api.deduce.TypeDeclarer;
import com.sum.spirit.api.deduce.VariableTracker;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

@Component
public class ElementVisiterImpl implements ElementVisiter {

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
	@Autowired
	public PostProcessor processor;

	@Override
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
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isCatch()) {
			Token varToken = element.getToken(3);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isAssign()) {
			Token varToken = element.getToken(0);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isForIn()) {
			Token varToken = element.getToken(1);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isReturn()) {
			Statement statement = element.subStmt(1, element.size());
			return new IVariable(deducer.derive(clazz, statement), null);
		}
		return null;
	}

}
