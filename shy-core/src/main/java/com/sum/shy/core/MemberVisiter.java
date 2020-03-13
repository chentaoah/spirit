package com.sum.shy.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IAnnotation;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IParameter;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;

public class MemberVisiter {

	public static void visit(Map<String, IClass> allClasses) {
		// 先解析方法入参类型
		for (IClass clazz : allClasses.values()) {
			for (IMethod method : clazz.methods)
				visitParameters(clazz, method);
		}
		// 开始推导返回类型
		for (IClass clazz : allClasses.values()) {
			for (AbsMember member : clazz.getAllMembers())
				visitMember(clazz, member);
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
			if (type != null) {
				member.setType(type);
			} else {
				throw new RuntimeException("Failed to derive member type!");
			}
		}
		member.unLock();
		return type;
	}

	public static void visitParameters(IClass clazz, IMethod method) {
		Element element = method.element;
		Token methodToken = element.findToken(Constants.LOCAL_METHOD_TOKEN);
		if (methodToken == null)
			methodToken = element.findToken(Constants.TYPE_INIT_TOKEN);
		// 这个时候，所有的class还没有解析完成，查询className会报空指针
		List<Stmt> subStmts = methodToken.getStmt().subStmt("(", ")").split(",");
		for (Stmt paramStmt : subStmts) {
			IParameter parameter = new IParameter();
			for (Token token : paramStmt.tokens) {
				if (token.isAnnotation()) {
					parameter.annotations.add(new IAnnotation(token));
				} else if (token.isType()) {
					parameter.type = new CodeType(clazz, token);
				} else if (token.isVar()) {
					parameter.name = token.toString();
				}
			}
			method.parameters.add(parameter);
		}
	}

	public static IType visitField(IClass clazz, IField field) {
		return ElementVisiter.visit(clazz, null, field.element).type;
	}

	public static IType visitMethod(IClass clazz, IMethod method) {
		// 解析返回类型
		MethodContext context = new MethodContext();
		context.method = method;
		visitChildElement(clazz, context, method.element);
		return context.returnType != null ? context.returnType : new CodeType(clazz, Constants.VOID);
	}

	public static void visitChildElement(IClass clazz, MethodContext context, Element father) {
		for (Element element : father) {
			Variable variable = ElementVisiter.visit(clazz, context, element);
			if (!element.isReturn() && variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);

			} else if (element.isReturn() && variable != null) {
				// 如果返回值更加抽象，则取代原来的
				if (context.returnType == null || variable.type.isAssignableFrom(context.returnType))
					context.returnType = variable.type;
			}
			if (element.size() > 0) {
				context.increaseDepth();
				visitChildElement(clazz, context, element);
				context.increaseCount();
				context.decreaseDepth();
			}
		}
	}

	public static class MethodContext {// 方法上下文
		public IMethod method;
		public List<Variable> variables = new ArrayList<>();
		public int depth = 0;// 深度
		public List<Integer> counts = new ArrayList<>(16);
		public IType returnType;// 返回

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
