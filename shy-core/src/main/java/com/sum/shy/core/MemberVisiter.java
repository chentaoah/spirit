package com.sum.shy.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.proc.FastDeducer;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;

public class MemberVisiter {

	public static void visit(Map<String, IClass> allClasses) {
		for (IClass clazz : allClasses.values())
			visit(clazz);
	}

	public static void visit(IClass clazz) {
		for (AbsMember member : clazz.getAllMembers())
			visitMember(clazz, member);
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

	private static IType visitField(IClass clazz, IField field) {
		Map<String, Object> result = ElementVisiter.visit(clazz, null, field.element);
		return (IType) result.get(FastDeducer.TYPE);
	}

	private static IType visitMethod(IClass clazz, IMethod method) {
		MethodContext context = new MethodContext();
		context.method = method;
		visitChildElement(clazz, context, method.element);
		return context.returnType != null ? context.returnType : new CodeType(clazz, Constants.VOID_TYPE);
	}

	private static void visitChildElement(IClass clazz, MethodContext context, Element father) {
		for (Element element : father) {
			Map<String, Object> result = ElementVisiter.visit(clazz, context, element);
			if (!element.isReturn() && result != null) {
				Variable variable = new Variable();
				variable.type = (IType) result.get(FastDeducer.TYPE);
				variable.name = (String) result.get(FastDeducer.NAME);
				variable.blockId = context.getBlockId();
				context.variables.add(variable);

			} else if (element.isReturn() && result != null) {
				IType returnType = (IType) result.get(FastDeducer.TYPE);
				if (context.returnType == null) {
					context.returnType = returnType;
				} else {
					// 如果返回值更加抽象，则取代原来的
					if (returnType.isAssignableFrom(context.returnType))
						context.returnType = returnType;
				}
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
