package com.sum.spirit.core.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.ElementBuilder;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

@Component
public class ExpressDeclarer {

	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter elementVisiter;
	@Autowired
	public VariableTracker tracker;
	@Autowired
	public InvokeVisiter visiter;
	@Autowired
	public FastDeducer deducer;

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
				varToken.setAttribute(AttributeEnum.DERIVED, true);
			}

			varToken.setAttribute(AttributeEnum.TYPE, type);

		} else if (element.isForIn()) {// for item in list {
			Statement statement = element.subStmt(3, element.size() - 1);
			tracker.track(clazz, context, statement);
			visiter.visit(clazz, statement);
			IType type = deducer.derive(clazz, statement);
			// Get internal type from array or generic type
			type = type.isArray() ? type.getTargetType() : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			varToken.setAttribute(AttributeEnum.TYPE, type);

		} else if (element.isFor()) {// for (i=0; i<100; i++) {
			Token secondToken = element.getToken(1);
			if (secondToken.isSubexpress()) {
				Statement statement = secondToken.getValue();
				Statement subStatement = statement.subStmt(1, statement.indexOf(";"));
				Element subElement = builder.rebuild(subStatement);
				IVariable variable = elementVisiter.visit(clazz, context, subElement);
				if (variable != null) {
					variable.blockId = context.getBlockId();
					context.variables.add(variable);
				}
			}
//			System.out.println(element.syntaxTree.debug());
		}
	}

}
