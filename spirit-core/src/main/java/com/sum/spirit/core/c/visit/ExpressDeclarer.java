package com.sum.spirit.core.c.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.ElementBuilder;
import com.sum.spirit.core.ElementVisiter;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

@Component
public class ExpressDeclarer {

	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter elementVisiter;
	@Autowired
	public VariableTracker tracker;
	@Autowired
	public InvokeVisiter visiter;
	@Autowired
	public FastDeducer deducer;

	public void declare(IClass clazz, MethodContext context, Element element) {

		if (element.isAssign()) {// text = "abc"
			Token varToken = element.getToken(0);
			IType type = null;
			// 如果有上下文，则先从上下文中找
			if (context != null) {
				type = tracker.findType(clazz, context, varToken.toString());
			}
			// 如果找不到，则必须通过推导获取类型
			if (type == null) {
				Statement statement = element.subStmt(2, element.size());
				tracker.track(clazz, context, statement);
				visiter.visit(clazz, statement);
				type = deducer.derive(clazz, statement);
				// 标记类型是否经过推导而来
				varToken.setAttr(AttributeEnum.DERIVED, true);
			}
			varToken.setAttr(AttributeEnum.TYPE, type);

		} else if (element.isForIn()) {// for item in list {
			Statement statement = element.subStmt(3, element.size() - 1);
			tracker.track(clazz, context, statement);
			visiter.visit(clazz, statement);
			IType type = deducer.derive(clazz, statement);
			// 获取数组内部类型和泛型类型
			type = type.isArray() ? type.getTargetType() : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			varToken.setAttr(AttributeEnum.TYPE, type);

		} else if (element.isFor()) {// for (i=0; i<100; i++) {
			Token secondToken = element.getToken(1);
			if (secondToken.isSubexpress()) {
				Statement statement = secondToken.getValue();
				Statement subStatement = statement.subStmt(1, statement.indexOf(";"));
				Element subElement = builder.rebuild(subStatement);
				IVariable variable = elementVisiter.visit(clazz, context, subElement);
				if (variable != null) {
					variable.blockId = context.getBlockId();
					context.variables.add(variable);
				}
			}
		}
	}

}
