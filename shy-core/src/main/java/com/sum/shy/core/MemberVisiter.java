package com.sum.shy.core;

import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.proc.InvokeVisiter;
import com.sum.shy.core.proc.StmtPreviewer;
import com.sum.shy.core.proc.TypeDeducer;
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
		// TODO Auto-generated method stub
		return null;
	}

	private static IType visitMethod(IClass clazz, IMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	public static IType visitElement(IClass clazz, Element element) {
		// 1.预览,为一些特殊语句提前声明一些变量
		StmtPreviewer.preview(clazz, element);
		// 2.变量追踪
		VariableTracker.trackStmt(clazz, element.stmt);
		// 3.调用推导
		InvokeVisiter.visitStmt(clazz, element.stmt);
		// 4.类型进行推导
		return TypeDeducer.derive(clazz, element);
	}

}
