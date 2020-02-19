package com.sum.shy.core;

import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.proc.InvokeVisiter;
import com.sum.shy.core.proc.SpecialDeclarer;
import com.sum.shy.core.proc.TypeDeclarer;
import com.sum.shy.core.proc.FastDeducer;
import com.sum.shy.core.proc.VariableTracker;
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
			if (type != null)
				member.setType(type);

			throw new RuntimeException("Failed to derive member type!");
		}
		member.unLock();
		return type;
	}

	private static IType visitField(IClass clazz, IField field) {
		Element element = field.element;
		if (element.isDeclare() || element.isDeclareAssign()) {
			// 1.类型声明者
			TypeDeclarer.declareStmt(clazz, element.stmt);
		}
		// 2.特殊语句的处理

		// 3.变量追踪
		VariableTracker.trackStmt(clazz, element.stmt);
		// 4.调用推导
		InvokeVisiter.visitStmt(clazz, element.stmt);

		return null;
	}

	private static IType visitMethod(IClass clazz, IMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void visitElement(IClass clazz, Element element) {

	}

}
