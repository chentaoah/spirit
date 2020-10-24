package com.sum.spirit.core;

import java.util.List;
import java.util.Map;

import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.AbsMember;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.utils.SpringUtils;

public abstract class AbsMemberVisiter {

	public void visit(Map<String, IClass> allClasses) {
		for (IClass clazz : allClasses.values()) {
			visitAnnotations(clazz, clazz.annotations);
			clazz.fields.forEach((field) -> visitAnnotations(clazz, field.annotations));
			clazz.methods.forEach((method) -> visitAnnotations(clazz, method.annotations));
		}
		for (IClass clazz : allClasses.values())
			clazz.methods.forEach((method) -> visitParameters(clazz, method));

		for (IClass clazz : allClasses.values()) {
			clazz.fields.forEach((field) -> visitMember(clazz, field));
			clazz.methods.forEach((method) -> visitMember(clazz, method));
		}
	}

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

	public void visitAnnotations(IClass clazz, List<IAnnotation> annotations) {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		annotations.forEach((annotation) -> annotation.type = factory.create(clazz, annotation.token));
	}

	public abstract void visitParameters(IClass clazz, IMethod method);

	public abstract IType visitField(IClass clazz, IField field);

	public abstract IType visitMethod(IClass clazz, IMethod method);

}
