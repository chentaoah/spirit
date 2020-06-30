package com.sum.shy.core.deduce;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.ElementVisiter;
import com.sum.shy.api.deduce.ExpressDeclarer;
import com.sum.shy.api.deduce.FastDeducer;
import com.sum.shy.api.deduce.InvokeVisiter;
import com.sum.shy.api.deduce.VariableTracker;
import com.sum.shy.api.lexer.ElementBuilder;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.clazz.IVariable;
import com.sum.shy.pojo.common.MethodContext;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;

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
				Statement subStmt = element.subStmt(2, element.size());
				tracker.track(clazz, context, subStmt);
				visiter.visit(clazz, subStmt);
				type = deducer.derive(clazz, subStmt);

				// After marking, it is convenient for subsequent conversion to
				// Java code and
				// automatic addition of type
				varToken.setDerived(true);
			}

			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {// for item in list {

			Statement subStmt = element.subStmt(3, element.size() - 1);
			tracker.track(clazz, context, subStmt);
			visiter.visit(clazz, subStmt);
			IType type = deducer.derive(clazz, subStmt);

			// Get internal type from array or generic type
			type = type.isArray() ? type.getTargetType() : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(type);

		} else if (element.isFor()) {// for i=0; i<100; i++ {

			// Note that although the sub statement is obtained here, it has
			// nothing to do
			// with the original statement itself
			Statement subStmt = element.subStmt(1, element.indexOf(";"));
			Element subElement = builder.build(subStmt.toString());

			// This step cannot be omitted. The accessor needs to mark the token
			// of the
			// original statement
			subElement.stmt = subStmt;

			IVariable variable = elementVisiter.visit(clazz, context, subElement);
			if (variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);
			}
		}
	}

}
