package com.sum.shy.api;

import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.AbsMember;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.IType;

@Service("member_visiter")
public interface MemberVisiter {

	default void visit(Map<String, IClass> allClasses) {

		for (IClass clazz : allClasses.values())
			clazz.methods.forEach((method) -> visitParameters(clazz, method));

		for (IClass clazz : allClasses.values()) {
			clazz.fields.forEach((field) -> visitMember(clazz, field));
			clazz.methods.forEach((method) -> visitMember(clazz, method));
		}

	}

	default IType visitMember(IClass clazz, AbsMember member) {
		// Avoid circular dependence
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
		// Avoid circular dependence
		member.unLock();
		return type;
	}

	void visitParameters(IClass clazz, IMethod method);

	IType visitField(IClass clazz, IField field);

	IType visitMethod(IClass clazz, IMethod method);

}
