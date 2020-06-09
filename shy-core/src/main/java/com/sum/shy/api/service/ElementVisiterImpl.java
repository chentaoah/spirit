package com.sum.shy.api.service;

import com.sum.shy.api.ElementVisiter;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.element.Element;
import com.sum.shy.processor.ExpressDeclarer;
import com.sum.shy.processor.FastDeducer;
import com.sum.shy.processor.InvokeVisiter;
import com.sum.shy.processor.TypeDeclarer;
import com.sum.shy.processor.VariableTracker;

public class ElementVisiterImpl implements ElementVisiter {

	@Override
	public IVariable visit(IClass clazz, MethodContext context, Element element) {
		try {
			// 1.类型声明者
			TypeDeclarer.declare(clazz, element);
			// 2.特殊语句的处理
			ExpressDeclarer.declare(clazz, context, element);
			// 3.变量追踪
			VariableTracker.trackStmt(clazz, context, element.stmt);
			// 4.调用推导
			InvokeVisiter.visitStmt(clazz, element.stmt);
			// 5.快速推导
			return FastDeducer.derive(clazz, element);

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
