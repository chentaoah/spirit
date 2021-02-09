package com.sum.spirit.core.visiter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.utils.ConfigUtils;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IField;
import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.IParameter;
import com.sum.spirit.core.clazz.entity.IVariable;
import com.sum.spirit.core.clazz.frame.MemberUnit;
import com.sum.spirit.core.element.ElementBuilder;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.core.visiter.entity.MethodContext;
import com.sum.spirit.core.visiter.enums.TypeEnum;
import com.sum.spirit.core.visiter.linker.TypeFactory;
import com.sum.spirit.core.visiter.utils.HeadVisiter;

import cn.hutool.core.lang.Assert;

@Component
public class ClassVisiter {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter visiter;
	@Autowired
	public ClassLinker linker;

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

	public IType visitMethod(IClass clazz, IMethod method) {
		// 方法上下文
		MethodContext context = new MethodContext(method);
		// 访问方法体内容
		visitChildElements(clazz, context, method.element);
		// 判断方法的语法
		if (method.element.isFunc()) {
			return context.returnType != null ? context.returnType : TypeEnum.void_t.value;

		} else if (method.element.isFuncDeclare()) {
			// 获取声明的类型
			IType declaredType = factory.create(clazz, method.element.getToken(0));
			// 如果这个方法有方法体
			if (method.element.hasChild()) {
				IType returnType = context.returnType != null ? context.returnType : TypeEnum.void_t.value;
				// 进行类型校验
				if (!linker.isMoreAbstract(declaredType, returnType)) {
					throw new RuntimeException("The derived type does not match the declared type!");
				}
			}
			// 最终返回声明的类型
			return declaredType;
		}
		throw new RuntimeException("Unsupported syntax!");
	}

	public void visitChildElements(IClass clazz, MethodContext context, Element father) {

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
			IVariable variable = visiter.visitElement(clazz, element, context);
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
						if (linker.isMoreAbstract(variable.getType(), context.returnType)) {
							context.returnType = variable.getType();
						} else {
							context.returnType = TypeEnum.Object.value;
						}
					}
				}
			}

			if (element.isReturn()) {
				// 语法校验
				if (index != elements.size() - 1) {
					if (ConfigUtils.isSyntaxCheck()) {
						throw new RuntimeException("The method body does not end with a return statement!");
					}
				}
				// 提前结束
				break;
			}

			// 遍历子节点
			if (element.children.size() > 0) {
				visitChildElements(clazz, context, element);
				context.increaseCount();
				context.decreaseDepth();
			}
		}
	}
}
