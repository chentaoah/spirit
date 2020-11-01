package com.sum.spirit.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.deduce.ElementVisiter;
import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.IParameter;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

@Component
public class MemberVisiter extends AbsMemberVisiter {

	@Autowired
	public ElementVisiter visiter;
	@Autowired
	public TypeFactory factory;

	public void visitParameters(IClass clazz, IMethod method) {
		// invoke() // User()
		Token methodToken = method.element.findToken(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
		Statement statement = methodToken.getValue();
		List<Statement> statements = statement.subStmt("(", ")").splitStmt(",");
		for (Statement paramStmt : statements) {
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

	public IType visitField(IClass clazz, IField field) {
		IVariable variable = visiter.visit(clazz, null, field.element);
		return variable.type;
	}

	public IType visitMethod(IClass clazz, IMethod method) {

		MethodContext context = new MethodContext(method);
		visitChildElement(clazz, context, method.element);

		if (method.element.isFunc()) {
			return context.returnType != null ? context.returnType : TypeEnum.VOID.value;

		} else if (method.element.isFuncDeclare()) {

			IType declaredType = factory.create(clazz, method.element.getToken(0));

			if (method.element.hasChild()) {
				IType returnType = context.returnType != null ? context.returnType : TypeEnum.VOID.value;
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
