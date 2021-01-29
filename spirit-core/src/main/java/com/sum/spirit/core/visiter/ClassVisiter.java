package com.sum.spirit.core.visiter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.IVariable;
import com.sum.spirit.core.common.enums.TypeEnum;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.core.visiter.entity.MethodContext;
import com.sum.spirit.utils.ConfigUtils;

@Component
public class ClassVisiter extends AbstractClassVisiter {

	@Autowired
	public ClassLinker linker;

	@Override
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
