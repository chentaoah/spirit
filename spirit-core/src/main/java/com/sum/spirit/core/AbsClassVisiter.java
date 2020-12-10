package com.sum.spirit.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.IParameter;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.clazz.api.IMember;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.utils.SpringUtils;

import cn.hutool.core.lang.Assert;

public abstract class AbsClassVisiter {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter visiter;

	public void visit(Map<String, IClass> allClasses) {
		// 解析所有的注解
		for (IClass clazz : allClasses.values()) {
			visitAnnotations(clazz, clazz.annotations);
			clazz.fields.forEach((field) -> visitAnnotations(clazz, field.annotations));
			clazz.methods.forEach((method) -> visitAnnotations(clazz, method.annotations));
		}
		// 解析类的类型
		for (IClass clazz : allClasses.values()) {
			clazz.setType(factory.create(clazz, clazz.getTypeToken()));
		}
		// 解析所有的方法入参
		for (IClass clazz : allClasses.values()) {
			clazz.methods.forEach((method) -> visitParameters(clazz, method));
		}
		// 解析所有字段和方法内容
		for (IClass clazz : allClasses.values()) {
			clazz.fields.forEach((field) -> visitMember(clazz, field));
			clazz.methods.forEach((method) -> visitMember(clazz, method));
		}
	}

	public void visitAnnotations(IClass clazz, List<IAnnotation> annotations) {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		annotations.forEach((annotation) -> annotation.setType(factory.create(clazz, annotation.token)));
	}

	public void visitParameters(IClass clazz, IMethod method) {
		// User() // invoke()
		Token methodToken = method.element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
		Statement statement = methodToken.getValue();
		List<Statement> statements = statement.subStmt("(", ")").splitStmt(",");
		for (Statement paramStmt : statements) {
			List<IAnnotation> annotations = getAnnotations(paramStmt);
			IParameter parameter = new IParameter(annotations, builder.rebuild(paramStmt));
			parameter.setType(factory.create(clazz, paramStmt.getToken(0)));
			method.parameters.add(parameter);
		}
	}

	public List<IAnnotation> getAnnotations(Statement paramStmt) {
		List<IAnnotation> annotations = new ArrayList<>();
		Iterator<Token> iterable = paramStmt.tokens.iterator();
		while (iterable.hasNext()) {
			Token token = iterable.next();
			if (token.isAnnotation()) {
				annotations.add(new IAnnotation(token));
				iterable.remove();
				continue;
			}
			break;
		}
		return annotations;
	}

	public IType visitMember(IClass clazz, IMember member) {
		// 防止循环依赖
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

	public IType visitField(IClass clazz, IField field) {
		IVariable variable = visiter.visitElement(clazz, field.element);
		return variable.getType();
	}

	public abstract IType visitMethod(IClass clazz, IMethod method);

}
