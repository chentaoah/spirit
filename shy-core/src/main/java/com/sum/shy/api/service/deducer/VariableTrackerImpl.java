package com.sum.shy.api.service.deducer;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberLinker;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.TypeFactory;
import com.sum.shy.api.VariableTracker;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.IParameter;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.common.Constants;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Assert;

public class VariableTrackerImpl implements VariableTracker {

	public MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public MemberLinker linker = ProxyFactory.get(MemberLinker.class);

	public TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void track(IClass clazz, MethodContext context, Statement stmt) {
		for (Token token : stmt.tokens) {
			if (token.canVisit())
				track(clazz, context, token.getStmt());

			if (token.getTypeAtt() != null)
				continue;

			if (token.isVar()) {// 如果没有设置类型的话
				String name = token.toString();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				token.setTypeAtt(type);

			} else if (token.isArrayIndex()) {// 如果没有设置类型的话
				String name = token.getMemberNameAtt();
				IType type = findType(clazz, context, name);// 返回的数组类型
				Assert.notNull(type, "Variable must be declared!name:" + name);
				type = factory.create(type.getTargetName());// 转换成数组内的类型
				token.setTypeAtt(type);
			}
		}
	}

	@Override
	public IType findType(IClass clazz, MethodContext context, String name) {

		// super引用,指向的是父类
		if (Constants.SUPER_KEYWORD.equals(name))
			return clazz.getSuperType();

		// this引用，指向的是这个类本身
		if (Constants.THIS_KEYWORD.equals(name))
			return factory.create(clazz.getClassName());

		// 先在方法上下文中找
		if (context != null) {
			IMethod method = context.method;
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			for (IVariable variable : context.variables) {
				if (variable.name.equals(name) && context.getBlockId().startsWith(variable.blockId))
					return variable.type;
			}
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (IParameter parameter : method.parameters) {
				if (parameter.name.equals(name))
					return parameter.type;
			}
		}

		// 成员变量
		for (IField field : clazz.fields) {
			if (field.name.equals(name)) {
				if (field.type == null)
					field.type = visiter.visitMember(clazz, field);
				return field.type;
			}
		}

		// 从继承里面去找，注意这里的父类可能是native的
		return linker.visitField(clazz.getSuperType(), name);
	}
}
