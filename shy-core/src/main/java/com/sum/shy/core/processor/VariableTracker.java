package com.sum.shy.core.processor;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IParameter;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.clazz.IVariable;
import com.sum.shy.core.clazz.type.TypeFactory;
import com.sum.shy.core.clazz.type.AdaptiveLinker;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.lib.Assert;

/**
 * 变量追踪器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年11月1日
 */
public class VariableTracker {

	public static void trackStmt(IClass clazz, MethodContext context, Stmt stmt) {
		for (Token token : stmt.tokens) {

			if (token.canVisit())
				trackStmt(clazz, context, token.getStmt());

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
				type = TypeFactory.create(type.getTargetName());// 转换成数组内的类型
				token.setTypeAtt(type);

			}

		}

	}

	public static IType findType(IClass clazz, MethodContext context, String name) {

		// super引用,指向的是父类
		if (Constants.SUPER_KEYWORD.equals(name))
			return clazz.getSuperType();

		// this引用，指向的是这个类本身
		if (Constants.THIS_KEYWORD.equals(name))
			return TypeFactory.create(clazz.getClassName());

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
					field.type = MemberVisiter.visitField(clazz, field);
				return field.type;
			}
		}

		// 从继承里面去找，注意这里的父类可能是native的
		return AdaptiveLinker.visitField(clazz.getSuperType(), name);

	}

}
