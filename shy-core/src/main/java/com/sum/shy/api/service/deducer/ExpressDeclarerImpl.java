package com.sum.shy.api.service.deducer;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deducer.ElementVisiter;
import com.sum.shy.api.deducer.ExpressDeclarer;
import com.sum.shy.api.deducer.FastDeducer;
import com.sum.shy.api.deducer.InvokeVisiter;
import com.sum.shy.api.deducer.VariableTracker;
import com.sum.shy.api.lexer.ElementBuilder;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.common.MethodContext;
import com.sum.shy.element.Element;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;

public class ExpressDeclarerImpl implements ExpressDeclarer {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	public static ElementVisiter visiter = ProxyFactory.get(ElementVisiter.class);

	public static VariableTracker tracker = ProxyFactory.get(VariableTracker.class);

	public static InvokeVisiter invokeVisiter = ProxyFactory.get(InvokeVisiter.class);

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void declare(IClass clazz, MethodContext context, Element element) {

		if (element.isAssign()) {// text = "abc"
			Token varToken = element.getToken(0);
			// 如果是field，那么直接推导，如果在方法中，则先从上下文中找一下
			IType type = null;
			if (context != null)
				type = tracker.findType(clazz, context, varToken.toString());
			if (type == null) {
				Statement subStmt = element.subStmt(2, element.size());
				tracker.track(clazz, context, subStmt);
				invokeVisiter.visit(clazz, subStmt);
				type = deducer.derive(clazz, subStmt);
				varToken.setDerivedAtt(true);// 标记类型由推导而来
			}
			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {// for item in list {
			Statement subStmt = element.subStmt(3, element.size() - 1);
			tracker.track(clazz, context, subStmt);
			invokeVisiter.visit(clazz, subStmt);
			IType type = deducer.derive(clazz, subStmt);
			// 这里从数组或集合中获取类型
			type = type.isArray() ? type.getTargetType() : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			varToken.setTypeAtt(type);

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Statement subStmt = element.subStmt(1, element.indexOf(";"));
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
