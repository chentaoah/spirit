package com.sum.shy.core;

import java.util.Map;

import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.proc.ExpressDeclarer;
import com.sum.shy.core.proc.FastDeducer;
import com.sum.shy.core.proc.InvokeVisiter;
import com.sum.shy.core.proc.TypeDeclarer;
import com.sum.shy.core.proc.VariableTracker;

public class ElementVisiter {

	/**
	 * 处理element对象，并根据语法返回类型，如果有的话
	 * 
	 * @param clazz
	 * @param context
	 * @param element
	 * @return
	 */
	public static Map<String, Object> visit(IClass clazz, MethodContext context, Element element) {
		// 1.类型声明者
		TypeDeclarer.declareStmt(clazz, element.stmt);
		// 2.特殊语句的处理
		ExpressDeclarer.declare(clazz, context, element);
		// 3.变量追踪
		VariableTracker.trackStmt(clazz, context, element.stmt);
		// 4.调用推导
		InvokeVisiter.visitStmt(clazz, element.stmt);
		// 5.快速推导
		return FastDeducer.derive(clazz, element);
	}

}
