package com.sum.shy.core;

import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.processor.ExpressDeclarer;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.processor.InvokeVisiter;
import com.sum.shy.core.processor.TypeDeclarer;
import com.sum.shy.core.processor.VariableTracker;

public class ElementVisiter {

	/**
	 * 处理element对象，并根据语法返回类型，如果有的话
	 * 
	 * @param clazz
	 * @param context
	 * @param element
	 * @return
	 */
	public static Variable visit(IClass clazz, MethodContext context, Element element) {
		try {
			// 1.类型声明者
			TypeDeclarer.declareStmt(clazz, element.stmt);
			// 2.特殊语句的处理
			ExpressDeclarer.declare(clazz, context, element);
			// 3.变量追踪
			VariableTracker.trackStmt(clazz, context, element.stmt);
			// 4.调用推导
			InvokeVisiter.visitStmt(clazz, element.stmt);
			// 5.快速推导
			return FastDeducer.derive(clazz, context, element);

		} catch (Exception e) {
			System.out.println("An exception has occurred!");
			System.out.println("ClassName:[" + clazz.getClassName() + "]");
			if (context != null)
				System.out.println("MethodName:[" + context.method.name + "]");
			element.debug();
			throw e;
		}

	}

}
