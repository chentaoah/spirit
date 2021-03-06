package com.sum.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.core.api.ElementBuilder;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.entity.IVariable;
import com.sum.spirit.core.compile.ElementVisiter;
import com.sum.spirit.core.compile.deduce.SimpleDeducer;
import com.sum.spirit.core.compile.deduce.InvocationVisiter;
import com.sum.spirit.core.compile.deduce.TypeDerivator;
import com.sum.spirit.core.compile.deduce.VariableTracker;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.compile.entity.MethodContext;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

@Component
@Order(-80)
public class ElementExpressDeclarer extends AbstractElementAction {

	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisiter elementVisiter;
	@Autowired
	public VariableTracker tracker;
	@Autowired
	public InvocationVisiter visiter;
	@Autowired
	public SimpleDeducer deducer;
	@Autowired
	public TypeDerivator derivator;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		MethodContext context = event.context;
		Element element = event.element;
		if (element.isAssign()) {// text = "abc"
			Token varToken = element.get(0);
			// 如果是字段声明，则不用进行上下文推导
			IType type = event.isMethodScope() ? tracker.findVariableType(clazz, context, varToken.toString()) : null;
			// 如果找不到，则必须通过推导获取类型
			if (type == null) {
				Statement statement = element.subStmt(2, element.size());
				tracker.visit(clazz, context, statement);
				visiter.visit(clazz, statement);
				type = deducer.derive(clazz, statement);
				// 标记类型是否经过推导而来
				varToken.setAttr(AttributeEnum.DERIVED, true);
			}
			varToken.setAttr(AttributeEnum.TYPE, type);
		}
		super.visit(event);
	}

	@Override
	public void visitMethodScope(ElementEvent event) {
		IClass clazz = event.clazz;
		MethodContext context = event.context;
		Element element = event.element;
		if (element.isForIn()) {// for item in list {
			Statement statement = element.subStmt(3, element.size() - 1);
			tracker.visit(clazz, context, statement);
			visiter.visit(clazz, statement);
			IType type = deducer.derive(clazz, statement);
			// 获取数组内部类型和泛型类型
			type = type.isArray() ? derivator.toTarget(type) : type.getGenericTypes().get(0);
			Token varToken = element.get(1);
			varToken.setAttr(AttributeEnum.TYPE, type);

		} else if (element.isFor()) {// for (i=0; i<100; i++) {
			Token secondToken = element.get(1);
			if (secondToken.isSubexpress()) {
				Statement statement = secondToken.getValue();
				Statement subStatement = statement.subStmt(1, statement.indexOf(";"));
				Element subElement = builder.rebuild(subStatement);
				IVariable variable = elementVisiter.visitElement(clazz, subElement, context);
				if (variable != null) {
					variable.blockId = context.getBlockId();
					context.variables.add(variable);
				}
			}
		}
	}

}
