package com.sum.shy.api.service.deducer;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.ElementBuilder;
import com.sum.shy.api.ElementVisiter;
import com.sum.shy.api.ExpressDeclarer;
import com.sum.shy.api.FastDeducer;
import com.sum.shy.api.InvokeVisiter;
import com.sum.shy.api.VariableTracker;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.element.Element;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;

public class ExpressDeclarerImpl implements ExpressDeclarer {

	public ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	public ElementVisiter visiter = ProxyFactory.get(ElementVisiter.class);

	public VariableTracker tracker = ProxyFactory.get(VariableTracker.class);

	public InvokeVisiter invokeVisiter = ProxyFactory.get(InvokeVisiter.class);

	public FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void declare(IClass clazz, MethodContext context, Element element) {

		if (element.isAssign()) {// text = "abc"
			Token varToken = element.getToken(0);
			// 如果是field，那么直接推导，如果在方法中，则先从上下文中找一下
			IType type = null;
			if (context != null)
				type = tracker.findType(clazz, context, varToken.toString());
			if (type == null) {
				Stmt subStmt = element.subStmt(2, element.size());
				tracker.trackStmt(clazz, context, subStmt);
				invokeVisiter.visitStmt(clazz, subStmt);
				type = deducer.deriveStmt(clazz, subStmt);
				varToken.setDerivedAtt(true);// 标记类型由推导而来
			}
			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {// for item in list {
			Stmt subStmt = element.subStmt(3, element.size() - 1);
			tracker.trackStmt(clazz, context, subStmt);
			invokeVisiter.visitStmt(clazz, subStmt);
			IType type = deducer.deriveStmt(clazz, subStmt);
			// 这里从数组或集合中获取类型
			type = type.isArray() ? type.getTargetType() : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(type);

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Stmt subStmt = element.subStmt(1, element.indexOf(";"));
			Element subElement = builder.build(subStmt.toString());
			subElement.stmt = subStmt;// 替换一下
			IVariable variable = visiter.visit(clazz, context, subElement);
			if (variable != null) {
				variable.blockId = context.getBlockId();
				context.variables.add(variable);
			}
		}
	}

}
