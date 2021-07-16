package com.gitee.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.api.ElementVisitor;
import com.gitee.spirit.core.api.StatementDeducer;
import com.gitee.spirit.core.api.VariableTracker;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

@Component
@Order(-80)
public class ExpressDeclareAction extends AbstractAppElementAction {

	@Autowired
	public VariableTracker tracker;
	@Autowired
	public VariableTrackAction variableAction;
	@Autowired
	public InvocationVisitAction invokeAction;
	@Autowired
	public StatementDeducer deducer;
	@Autowired
	public ElementBuilder builder;
	@Autowired
	public ElementVisitor visitor;

	@Override
	public void visitElement(VisitContext context, Element element) {
		if (element.isAssign()) {// text = "abc"
			Token varToken = element.get(0);
			// 如果是字段声明，则不用进行上下文推导
			IType type = context.isMethodScope() ? tracker.findVariableType(context, varToken.toString()) : null;
			// 如果找不到，则必须通过推导获取类型
			if (type == null) {
				Statement statement = element.subStmt(2, element.size());
				Element subElement = new Element(statement);
				variableAction.visitElement(context, subElement);
				invokeAction.visitElement(context, subElement);
				type = deducer.derive(statement);
				// 标记类型是否经过推导而来
				varToken.setAttr(Attribute.DERIVED, true);
			}
			varToken.setAttr(Attribute.TYPE, type);
		}
		super.visitElement(context, element);
	}

	@Override
	public void visitMethodScope(VisitContext context, Element element) {
		if (element.isForIn()) {// for item in list {
			Statement statement = element.subStmt(3, element.size() - 1);
			Element subElement = new Element(statement);
			variableAction.visitElement(context, subElement);
			invokeAction.visitElement(context, subElement);
			IType type = deducer.derive(statement);
			// 获取数组内部类型和泛型类型
			type = type.isArray() ? type.toTarget() : type.getGenericTypes().get(0);
			Token varToken = element.get(1);
			varToken.setAttr(Attribute.TYPE, type);

		} else if (element.isFor()) {// for (i=0; i<100; i++) {
			Token secondToken = element.get(1);
			if (secondToken.isSubexpress()) {
				Statement statement = secondToken.getValue();
				Statement subStatement = statement.subStmt(1, statement.indexOf(";"));
				if (subStatement.size() > 0) {
					Element subElement = builder.build(subStatement);
					IVariable variable = visitor.visitElement(context, subElement);
					if (variable != null) {
						variable.blockId = context.getBlockId();
						context.variables.add(variable);
					}
				}
			}
		}
	}

}
