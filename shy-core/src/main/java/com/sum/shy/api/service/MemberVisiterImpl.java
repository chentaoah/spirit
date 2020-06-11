package com.sum.shy.api.service;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.deducer.ElementVisiter;
import com.sum.shy.api.deducer.TypeFactory;
import com.sum.shy.clazz.IAnnotation;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.IParameter;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.common.Constants;
import com.sum.shy.common.MethodContext;
import com.sum.shy.common.StaticType;
import com.sum.shy.element.Element;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;

public class MemberVisiterImpl implements MemberVisiter {

	public static ElementVisiter visiter = ProxyFactory.get(ElementVisiter.class);

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void visitParameters(IClass clazz, IMethod method) {
		// invoke() // User()
		Token methodToken = method.element.findToken(Constants.TYPE_INIT_TOKEN, Constants.LOCAL_METHOD_TOKEN);
		Statement stmt = methodToken.getValue();
		List<Statement> subStmts = stmt.subStmt("(", ")").split(",");
		for (Statement paramStmt : subStmts) {
			IParameter parameter = new IParameter();
			for (Token token : paramStmt.tokens) {
				if (token.isAnnotation()) {
					parameter.annotations.add(new IAnnotation(token));

				} else if (token.isType()) {
					parameter.type = factory.create(clazz, token);

				} else if (token.isVar()) {
					parameter.name = token.toString();
				}
			}
			method.parameters.add(parameter);
		}
	}

	@Override
	public IType visitField(IClass clazz, IField field) {
		IVariable variable = visiter.visit(clazz, null, field.element);
		return variable.type;
	}

	@Override
	public IType visitMethod(IClass clazz, IMethod method) {
		if (method.element.isFuncDeclare()) {
			return factory.create(clazz, method.element.getToken(0));

		} else if (method.element.isFunc()) {
			MethodContext context = new MethodContext(method);
			visitChildElement(clazz, context, method.element);
			return context.returnType != null ? context.returnType : StaticType.VOID_TYPE;
		}
		throw new RuntimeException("Unsupported syntax!");
	}

	public void visitChildElement(IClass clazz, MethodContext context, Element father) {
		for (Element element : father.children) {

			// The depth must be increased in advance so that the block ID generation is not
			// problematic
			if (element.children.size() > 0)
				context.increaseDepth();

			IVariable variable = visiter.visit(clazz, context, element);
			if (!element.isReturn() && variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);

			} else if (element.isReturn() && variable != null) {
				// If there is no return type, accept most types
				if (context.returnType == null) {
					context.returnType = variable.type;
				} else {
					// If there is already a specific return type, ignore null
					if (!variable.type.isNull()) {
						// If there are multiple return statements, take the most abstract return type
						if (variable.type.isMatch(context.returnType)) {
							context.returnType = variable.type;
						} else {
							throw new RuntimeException("Return type does not match!");
						}
					}
				}
			}

			if (element.children.size() > 0) {
				visitChildElement(clazz, context, element);
				context.increaseCount();
				context.decreaseDepth();
			}
		}
	}

}
