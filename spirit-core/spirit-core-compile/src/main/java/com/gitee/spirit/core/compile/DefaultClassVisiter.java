package com.gitee.spirit.core.compile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.common.utils.ObjectUtils;
import com.gitee.spirit.core.api.ClassVisiter;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.api.ElementVisiter;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IAnnotation;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IField;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IParameter;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.clazz.frame.MemberEntity;
import com.gitee.spirit.core.clazz.utils.TypeTable;
import com.gitee.spirit.core.compile.derivator.AppTypeDerivator;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class DefaultClassVisiter implements ClassVisiter {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter visiter;
	@Autowired
	public AppTypeDerivator derivator;

	@Override
	public void prepareForVisit(IClass clazz) {
		// 访问类型
		clazz.setType(factory.create(clazz, clazz.getTypeToken()));
		// 访问方法入参
		clazz.methods.forEach(method -> visitParameters(clazz, method));
	}

	@Override
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
		annotations.forEach(annotation -> annotation.setType(factory.create(clazz, annotation.token)));
	}

	public void visitParameters(IClass clazz, IMethod method) {
		// User() // invoke()
		Token methodToken = method.element.findOneTokenOf(TokenTypeEnum.TYPE_INIT, TokenTypeEnum.LOCAL_METHOD);
		Statement statement = methodToken.getValue();
		List<Statement> statements = statement.subStmt("(", ")").splitStmt(",");
		for (Statement parameterStmt : statements) {
			List<IAnnotation> annotations = ListUtils.filterStoppable(parameterStmt, token -> token.isAnnotation(), token -> new IAnnotation(token));
			IParameter parameter = new IParameter(annotations, builder.build(parameterStmt));
			parameter.setType(factory.create(clazz, parameterStmt.get(0)));
			method.parameters.add(parameter);
		}
	}

	@Override
	public IType visitMember(IClass clazz, MemberEntity member) {
		ObjectUtils.lock(member); // 防止循环依赖
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
		ObjectUtils.unlock(member);
		return type;
	}

	public IType visitField(IClass clazz, IField field) {
		IVariable variable = visiter.visitElement(new VisitContext(clazz, field), field.element);
		return variable.getType();
	}

	public IType visitMethod(IClass clazz, IMethod method) {
		// 方法上下文
		VisitContext context = new VisitContext(clazz, method);
		// 访问方法体内容
		visitChildElements(context, method.element);
		// 判断方法的语法
		if (method.element.isFunc()) {
			return context.returnType != null ? context.returnType : TypeTable.VOID;

		} else if (method.element.isDeclareFunc()) {
			// 获取声明的类型
			IType declaredType = factory.create(clazz, method.element.get(0));
			// 如果这个方法有方法体
			if (method.element.hasChild()) {
				IType returnType = context.returnType != null ? context.returnType : TypeTable.VOID;
				// 进行类型校验
				if (!derivator.isMoreAbstract(declaredType, returnType)) {
					throw new RuntimeException("The derived type does not match the declared type!");
				}
			}
			// 最终返回声明的类型
			return declaredType;
		}
		throw new RuntimeException("Unsupported syntax!");
	}

	public void visitChildElements(VisitContext context, Element father) {

		// 遍历所有子元素
		List<Element> elements = father.children;
		if (elements == null || elements.size() == 0) {
			return;
		}

		for (int index = 0; index < elements.size(); index++) {
			Element element = elements.get(index);
			// 提前将深度加一，以获得正确的blockId
			if (element.children.size() > 0) {
				context.increaseDepth();
			}
			// 对该元素进行分析
			IVariable variable = visiter.visitElement(context, element);
			// 如果该元素不是return语句，并且变量不为空，则将变量添加到上下文中
			if (!element.isReturn() && variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);

			} else if (element.isReturn() && variable != null) {
				// 如果上下文中还没有返回类型，则将变量的类型，作为返回类型
				if (context.returnType == null) {
					context.returnType = variable.getType();
				} else {
					// 如果有多个返回类型，则使用最抽象的那个
					if (!variable.getType().isNull()) {
						// 注意：任何类型都是null的抽象，null不能是任何类型的抽象
						if (derivator.isMoreAbstract(variable.getType(), context.returnType)) {
							context.returnType = variable.getType();
						} else {
							context.returnType = TypeTable.OBJECT;
						}
					}
				}
			}

			// 如果return语句后面还有语句，则抛出异常
			if (element.isReturn() && index != elements.size() - 1) {
				throw new RuntimeException("The method body does not end with a return statement!");
			}

			// 遍历子节点
			if (element.children.size() > 0) {
				visitChildElements(context, element);
				context.increaseCount();
				context.decreaseDepth();
			}
		}
	}
}
