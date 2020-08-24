package com.sum.spirit.core.deduce;

import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.deduce.ElementVisiter;
import com.sum.spirit.api.deduce.ExpressDeclarer;
import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.api.deduce.InvokeVisiter;
import com.sum.spirit.api.deduce.VariableTracker;
import com.sum.spirit.api.lexer.ElementBuilder;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

public class ExpressDeclarerImpl implements ExpressDeclarer {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	public static ElementVisiter elementVisiter = ProxyFactory.get(ElementVisiter.class);

	public static VariableTracker tracker = ProxyFactory.get(VariableTracker.class);

	public static InvokeVisiter visiter = ProxyFactory.get(InvokeVisiter.class);

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void declare(IClass clazz, MethodContext context, Element element) {

		if (element.isAssign()) {// text = "abc"

			Token varToken = element.getToken(0);
			IType type = null;

			// If there is a method context, it means that the method return
			// type is
			// currently being derived, then try to get the type from the
			// context first
			if (context != null)
				type = tracker.findType(clazz, context, varToken.toString());

			if (type == null) {
				Statement statement = element.subStmt(2, element.size());
				tracker.track(clazz, context, statement);
				visiter.visit(clazz, statement);
				type = deducer.derive(clazz, statement);

				// After marking, it is convenient for subsequent conversion to
				// Java code and
				// automatic addition of type
				varToken.setDerived(true);
			}

			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {// for item in list {

			Statement statement = element.subStmt(3, element.size() - 1);
			tracker.track(clazz, context, statement);
			visiter.visit(clazz, statement);
			IType type = deducer.derive(clazz, statement);

			// Get internal type from array or generic type
			type = type.isArray() ? type.getTargetType() : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(type);

		} else if (element.isFor()) {// for i=0; i<100; i++ {

			// Note that although the sub statement is obtained here, it has
			// nothing to do
			// with the original statement itself
			Statement statement = element.subStmt(1, element.indexOf(";"));
			Element subElement = builder.build(statement.toString());

			// This step cannot be omitted. The accessor needs to mark the token
			// of the
			// original statement
			subElement.statement = statement;

			IVariable variable = elementVisiter.visit(clazz, context, subElement);
			if (variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);
			}
		}
	}

}