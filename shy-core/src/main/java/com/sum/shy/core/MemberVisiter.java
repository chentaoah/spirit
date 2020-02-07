package com.sum.shy.core;

import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
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
		if (member.getType() == null) {
			if (member instanceof IField) {
//				IType type = process(clazz, member);
//				member.setType(type);

			} else if (member instanceof IMethod) {
//				IType type = process(clazz, member);
//				member.setType(type);
			}
		}
		member.unLock();
		return member.getType();
	}

//	public static IType process(IClass clazz, Element element) {
//		// 1.预览,为一些特殊语句提前声明一些变量
//		StmtPreviewer.preview(element);
//		// 2.变量追踪
//		VariableTracker.track(clazz, element);
//		// 3.调用推导
//		InvokeVisiter.visit(clazz, element);
//		// 4.对
//		return null;
//	}

}
