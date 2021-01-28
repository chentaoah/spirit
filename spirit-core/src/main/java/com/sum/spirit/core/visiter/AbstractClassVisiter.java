package com.sum.spirit.core.visiter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.clazz.pojo.IAnnotation;
import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.clazz.pojo.IField;
import com.sum.spirit.core.clazz.pojo.IMethod;
import com.sum.spirit.core.clazz.pojo.IParameter;
import com.sum.spirit.core.clazz.pojo.IVariable;
import com.sum.spirit.core.clazz.pojo.api.MemberUnit;
import com.sum.spirit.core.common.enums.TokenTypeEnum;
import com.sum.spirit.core.element.ElementBuilder;
import com.sum.spirit.core.element.pojo.Statement;
import com.sum.spirit.core.element.pojo.Token;
import com.sum.spirit.core.visiter.action.linker.TypeFactory;
import com.sum.spirit.core.visiter.pojo.IType;
import com.sum.spirit.core.visiter.utils.HeadVisiter;
import com.sum.spirit.utils.SpringUtils;

import cn.hutool.core.lang.Assert;

public abstract class AbstractClassVisiter {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter visiter;

	public void prepareForVisit(IClass clazz) {
		// 访问类型
		clazz.setType(factory.create(clazz, clazz.getTypeToken()));
		// 访问方法入参
		clazz.methods.forEach(method -> visitParameters(clazz, method));
	}

	public void visitClass(IClass clazz) {
		Assert.notNull(clazz.getType(), "Please invoke the method [prepareForVisit] first!");
		// 访问注解
		visitAnnotations(clazz, clazz.annotations);
		clazz.fields.forEach(field -> visitAnnotations(clazz, field.annotations));
		clazz.methods.forEach(method -> visitAnnotations(clazz, method.annotations));
		// 访问成员
		clazz.fields.forEach(field -> visitMember(clazz, field));
		clazz.methods.forEach(method -> visitMember(clazz, method));
	}

	public void visitAnnotations(IClass clazz, List<IAnnotation> annotations) {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		annotations.forEach(annotation -> annotation.setType(factory.create(clazz, annotation.token)));
	}

	public void visitParameters(IClass clazz, IMethod method) {
		// User() // invoke()
		Token methodToken = method.element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
		Statement statement = methodToken.getValue();
		List<Statement> statements = statement.subStmt("(", ")").splitStmt(",");
		for (Statement paramStmt : statements) {
			List<IAnnotation> annotations = new HeadVisiter<Token>().visit(paramStmt.tokens, token -> token.isAnnotation(), token -> new IAnnotation(token));
			IParameter parameter = new IParameter(annotations, builder.rebuild(paramStmt));
			parameter.setType(factory.create(clazz, paramStmt.getToken(0)));
			method.parameters.add(parameter);
		}
	}

	public IType visitMember(IClass clazz, MemberUnit member) {
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
