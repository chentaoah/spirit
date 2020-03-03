package com.sum.shy.core;

import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IParameter;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.type.api.IType;

public class MemberVisiter {

	public static void visit(Map<String, IClass> allClasses) {
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

	private static IType visitField(IClass clazz, IField field) {
		return ElementVisiter.visit(clazz, null, field.element);
	}

	private static IType visitMethod(IClass clazz, IMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	public class MethodContext {// 方法上下文
		public IMethod method;
		public Map<String, Variable> variables;
		public String blockId;// 块位置id
	}

}
