package com.sum.shy.core.deduce;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.PostProcessor;
import com.sum.shy.api.deduce.ElementVisiter;
import com.sum.shy.api.deduce.ExpressDeclarer;
import com.sum.shy.api.deduce.FastDeducer;
import com.sum.shy.api.deduce.InvokeVisiter;
import com.sum.shy.api.deduce.TypeDeclarer;
import com.sum.shy.api.deduce.VariableTracker;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IVariable;
import com.sum.shy.pojo.common.MethodContext;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;

public class ElementVisiterImpl implements ElementVisiter {

	public static TypeDeclarer declarer = ProxyFactory.get(TypeDeclarer.class);

	public static ExpressDeclarer expressDeclarer = ProxyFactory.get(ExpressDeclarer.class);

	public static VariableTracker tracker = ProxyFactory.get(VariableTracker.class);

	public static InvokeVisiter visiter = ProxyFactory.get(InvokeVisiter.class);

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	public static PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	@Override
	public IVariable visit(IClass clazz, MethodContext context, Element element) {
		try {
			// give a chance to change element
			processor.postBeforeVisitProcessor(clazz, context, element);

			// 1.some statements need to declare variable types in advance
			declarer.declare(clazz, element);

			// 2.some statements need to deduce the type of a token based on an expression
			expressDeclarer.declare(clazz, context, element);

			// 3.get the type of variable from context
			tracker.track(clazz, context, element.stmt);

			// 4.get the return value type of the method call
			visiter.visit(clazz, element.stmt);

			// give a chance to change element
			processor.postAfterVisitProcessor(clazz, context, element);

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
			Statement subStmt = element.subStmt(1, element.size());
			return new IVariable(deducer.derive(clazz, subStmt), null);
		}
		return null;
	}

}