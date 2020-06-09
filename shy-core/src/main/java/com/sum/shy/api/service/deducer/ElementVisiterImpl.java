package com.sum.shy.api.service.deducer;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.ElementVisiter;
import com.sum.shy.api.ExpressDeclarer;
import com.sum.shy.api.FastDeducer;
import com.sum.shy.api.InvokeVisiter;
import com.sum.shy.api.TypeDeclarer;
import com.sum.shy.api.VariableTracker;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.element.Element;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;

public class ElementVisiterImpl implements ElementVisiter {

	public TypeDeclarer declarer = ProxyFactory.get(TypeDeclarer.class);

	public ExpressDeclarer expressDeclarer = ProxyFactory.get(ExpressDeclarer.class);

	public VariableTracker tracker = ProxyFactory.get(VariableTracker.class);

	public InvokeVisiter visiter = ProxyFactory.get(InvokeVisiter.class);

	public FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public IVariable visit(IClass clazz, MethodContext context, Element element) {
		try {
			// 1.类型声明者
			declarer.declare(clazz, element);
			// 2.特殊语句的处理
			expressDeclarer.declare(clazz, context, element);
			// 3.变量追踪
			tracker.trackStmt(clazz, context, element.stmt);
			// 4.调用推导
			visiter.visitStmt(clazz, element.stmt);
			// 5.快速推导
			return derive(clazz, element);

		} catch (Exception e) {
			System.out.println("An exception has occurred!");
			System.out.println("ClassName:[" + clazz.getClassName() + "]");
			if (context != null)
				System.out.println("MethodName:[" + context.method.name + "]");
			element.debug();
			throw e;
		}

	}

	public IVariable derive(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign()) {
			Token varToken = element.getToken(1);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isCatch()) {
			Token varToken = element.getToken(3);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isAssign()) {
			Token varToken = element.getToken(0);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isForIn()) {
			Token varToken = element.getToken(1);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isReturn()) {
			Statement subStmt = element.subStmt(1, element.size());
			return new IVariable(deducer.deriveStmt(clazz, subStmt), null);
		}
		return null;
	}

}
