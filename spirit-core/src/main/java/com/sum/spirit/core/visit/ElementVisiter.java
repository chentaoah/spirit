package com.sum.spirit.core.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

@Component
public class ElementVisiter {

	@Autowired
	public TypeDeclarer declarer;
	@Autowired
	public ExpressDeclarer expressDeclarer;
	@Autowired
	public VariableTracker tracker;
	@Autowired
	public InvokeVisiter visiter;
	@Autowired
	public FastDeducer deducer;

	public IVariable visit(IClass clazz, MethodContext context, Element element) {
		try {
			// 1.一部分语句需要提前声明类型
			declarer.declare(clazz, element);
			// 2.一部分语句中的表达式，需要进行推导
			expressDeclarer.declare(clazz, context, element);
			// 3.变量追踪
			tracker.track(clazz, context, element.statement);
			// 4.方法推导
			visiter.visit(clazz, element.statement);
			// 5.判断语法是否声明了一个变量
			return getVariableIfPossible(clazz, element);

		} catch (Exception e) {
			element.debug();
			throw new RuntimeException("Failed to derive element!", e);
		}

	}

	public IVariable getVariableIfPossible(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign()) {
			Token varToken = element.getToken(1);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.attr(AttributeEnum.TYPE));
			return variable;

		} else if (element.isCatch()) {
			Token varToken = element.getToken(3);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.attr(AttributeEnum.TYPE));
			return variable;

		} else if (element.isAssign()) {
			Token varToken = element.getToken(0);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.attr(AttributeEnum.TYPE));
			return variable;

		} else if (element.isForIn()) {
			Token varToken = element.getToken(1);
			IVariable variable = new IVariable(varToken);
			variable.setType(varToken.attr(AttributeEnum.TYPE));
			return variable;

		} else if (element.isReturn()) {
			Statement statement = element.subStmt(1, element.size());
			IVariable variable = new IVariable(null);
			variable.setType(deducer.derive(clazz, statement));
			return variable;
		}
		return null;
	}

}
