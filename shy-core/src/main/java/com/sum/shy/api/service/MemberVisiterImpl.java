package com.sum.shy.api.service;

import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.deducer.ElementVisiter;
import com.sum.shy.api.deducer.TypeFactory;
import com.sum.shy.clazz.AbsMember;
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
import com.sum.shy.lib.Assert;

public class MemberVisiterImpl implements MemberVisiter {

	public static ElementVisiter visiter = ProxyFactory.get(ElementVisiter.class);

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void visit(Map<String, IClass> allClasses) {
		// 先解析方法入参类型
		for (IClass clazz : allClasses.values()) {
			for (IMethod method : clazz.methods)
				visitParameters(clazz, method);
		}
		// 开始推导返回类型
		for (IClass clazz : allClasses.values()) {
			for (IField field : clazz.fields)
				visitMember(clazz, field);
			for (IMethod method : clazz.methods)
				visitMember(clazz, method);
		}
	}

	public void visitParameters(IClass clazz, IMethod method) {
		// invoke() // User()
		Token methodToken = method.element.findToken(Constants.TYPE_INIT_TOKEN, Constants.LOCAL_METHOD_TOKEN);
		// 这个时候，所有的class还没有解析完成，查询className会报空指针
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
	public IType visitMember(IClass clazz, AbsMember member) {
		member.lock();
		IType type = member.getType();
		if (type == null) {
			if (member instanceof IField) {
				type = visitField(clazz, (IField) member);

			} else if (member instanceof IMethod) {
				type = visitMethod(clazz, (IMethod) member);
			}
			Assert.notNull(type, "Failed to derive member type!");
			member.setType(type);
		}
		member.unLock();
		return type;
	}

	public IType visitField(IClass clazz, IField field) {
		return visiter.visit(clazz, null, field.element).type;
	}

	public IType visitMethod(IClass clazz, IMethod method) {
		if (method.element.isFuncDeclare()) {// 声明了返回类型的方法，直接返回类型
			return factory.create(clazz, method.element.getToken(0));

		} else if (method.element.isFunc()) {
			MethodContext context = new MethodContext(method);
			visitChildElement(clazz, context, method.element);
			return context.returnType != null ? context.returnType : StaticType.VOID_TYPE;
		}
		return null;
	}

	public void visitChildElement(IClass clazz, MethodContext context, Element father) {
		for (Element element : father.children) {

			if (element.children.size() > 0)
				context.increaseDepth();// 提前深度+1

			IVariable variable = visiter.visit(clazz, context, element);
			if (!element.isReturn() && variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);

			} else if (element.isReturn() && variable != null) {
				if (!variable.type.isNull()) {
					if (context.returnType == null) {
						context.returnType = variable.type;
					} else {
						if (variable.type.isMatch(context.returnType)) {// 如果返回值更加抽象，则取代原来的
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
