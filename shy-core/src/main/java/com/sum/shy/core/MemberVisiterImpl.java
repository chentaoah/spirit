package com.sum.shy.core;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.deduce.ElementVisiter;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.pojo.clazz.IAnnotation;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.IParameter;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.clazz.IVariable;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.common.MethodContext;
import com.sum.shy.pojo.common.StaticType;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;

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

		MethodContext context = new MethodContext(method);
		visitChildElement(clazz, context, method.element);

		if (method.element.isFunc()) {
			return context.returnType != null ? context.returnType : StaticType.VOID_TYPE;

		} else if (method.element.isFuncDeclare()) {

			IType declaredType = factory.create(clazz, method.element.getToken(0));

			if (method.element.hasChildElement()) {
				IType returnType = context.returnType != null ? context.returnType : StaticType.VOID_TYPE;
				if (!declaredType.isMatch(returnType))
					throw new RuntimeException("The derived type does not match the declared type!");
			}

			return declaredType;
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
					// If there are multiple return statements, take the most abstract return type
					// Null can not match any type
					// Any type can match null
					if (variable.type.isMatch(context.returnType)) {
						context.returnType = variable.type;
					} else {
						if (!variable.type.isNull())
							throw new RuntimeException("Multiple return types do not match");
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
