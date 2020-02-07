package com.sum.shy.core;

import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.type.api.IType;

public class MemberDeducer {

	public static void derive(Map<String, IClass> allClasses) {
		for (IClass clazz : allClasses.values()) {
			for (AbsMember member : clazz.getAllMembers())
				visitMember(clazz, member);
		}
	}

	public static IType visitMember(IClass clazz, AbsMember member) {
		member.lock();
		if (member.getType() == null) {
			if (member instanceof IField) {

			} else if (member instanceof IMethod) {

			}
		}
		member.unLock();
		return member.getType();
	}

}
