package com.sum.shy.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.clazz.AbsMember;
import com.sum.shy.clazz.IAnnotation;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.IParameter;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.common.Constants;
import com.sum.shy.common.StaticType;
import com.sum.shy.deducer.TypeFactory;
import com.sum.shy.element.Element;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Assert;
import com.sum.shy.processor.ElementVisiter;

public class ElementMemberVisiter implements MemberVisiter {

	@Override
	public void visitMembers(Map<String, IClass> allClasses) {
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

	public static void visitParameters(IClass clazz, IMethod method) {
		// invoke() // User()
		Token methodToken = method.element.findToken(Constants.TYPE_INIT_TOKEN, Constants.LOCAL_METHOD_TOKEN);
		// 这个时候，所有的class还没有解析完成，查询className会报空指针
		List<Stmt> subStmts = methodToken.getStmt().subStmt("(", ")").split(",");
		for (Stmt paramStmt : subStmts) {
			IParameter parameter = new IParameter();
			for (Token token : paramStmt.tokens) {
				if (token.isAnnotation()) {
					parameter.annotations.add(new IAnnotation(token));

				} else if (token.isType()) {
					parameter.type = TypeFactory.create(clazz, token);

				} else if (token.isVar()) {
					parameter.name = token.toString();
				}
			}
			method.parameters.add(parameter);
		}
	}

	public static IType visitMember(IClass clazz, AbsMember member) {
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

	public static IType visitField(IClass clazz, IField field) {
		return ElementVisiter.visit(clazz, null, field.element).type;
	}

	public static IType visitMethod(IClass clazz, IMethod method) {
		if (method.element.isFuncDeclare()) {// 声明了返回类型的方法，直接返回类型
			return TypeFactory.create(clazz, method.element.getToken(0));

		} else if (method.element.isFunc()) {
			MethodContext context = new MethodContext(method);
			visitChildElement(clazz, context, method.element);
			return context.returnType != null ? context.returnType : StaticType.VOID_TYPE;
		}
		return null;
	}

	public static void visitChildElement(IClass clazz, MethodContext context, Element father) {
		for (Element element : father.children) {

			if (element.children.size() > 0)
				context.increaseDepth();// 提前深度+1

			IVariable variable = ElementVisiter.visit(clazz, context, element);
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

	public static class MethodContext {// 方法上下文
		public IMethod method;
		public List<IVariable> variables = new ArrayList<>();
		public int depth = 0;// 深度
		public List<Integer> counts = new ArrayList<>(16);
		public IType returnType;// 返回

		public MethodContext(IMethod method) {
			this.method = method;
		}

		public void increaseDepth() {
			depth = depth + 1;
		}

		public void decreaseDepth() {
			depth = depth - 1;
		}

		public void increaseCount() {
			while (depth >= counts.size())
				counts.add(0);
			counts.set(depth, counts.get(depth) + 1);
		}

		public String getBlockId() {
			while (depth >= counts.size())
				counts.add(0);
			return Joiner.on("-").join(counts.subList(0, depth + 1));
		}

	}

}
