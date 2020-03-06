package com.sum.shy.core.processor;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IParameter;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.core.visiter.AdaptiveVisiter;
import com.sum.shy.core.visiter.api.Visiter;
import com.sum.shy.lib.StringUtils;

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

	public static Visiter visiter = new AdaptiveVisiter();

	public static void trackStmt(IClass clazz, MethodContext context, Stmt stmt) {
		for (Token token : stmt.tokens) {
			if (token.hasStmt())
				trackStmt(clazz, context, token.getStmt());

			if (token.isVar() && token.getTypeAtt() == null) {// 如果没有设置类型的话
				String name = token.toString();
				IType type = findType(clazz, context, name);
				if (type == null)
					throw new RuntimeException("Variable must be declared!name:" + name);
				token.setTypeAtt(type);

			} else if (token.isArrayIndex() && token.getTypeAtt() == null) {// 如果没有设置类型的话
				String name = token.getMemberNameAtt();
				IType type = findType(clazz, context, name);
				if (type == null)
					throw new RuntimeException("Variable must be declared!name:" + name);
				token.setTypeAtt(type);
			}

		}

	}

	public static IType findType(IClass clazz, MethodContext context, String name) {

		// super引用,指向的是父类
		if (Constants.SUPER_KEYWORD.equals(name))
			return new CodeType(clazz, clazz.getSuperName());

		// this引用，指向的是这个类本身
		if (Constants.THIS_KEYWORD.equals(name))
			return new CodeType(clazz, clazz.getTypeName());

		// 先在方法上下文中找
		if (context != null) {
			IMethod method = context.method;
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			for (Variable variable : context.variables) {
				if (variable.name.equals(name) && context.getBlockId().startsWith(variable.blockId))
					return variable.type;
			}
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (IParameter parameter : method.getParameters()) {
				if (parameter.name.equals(name))
					return parameter.type;
			}
		}

		// 成员变量
		for (IField field : clazz.fields) {
			if (field.name.equals(name)) {
				if (field.type == null)
					return MemberVisiter.visitField(clazz, field);
			}
		}

		// 从继承里面去找，注意这里的父类可能是native的
		if (StringUtils.isNotEmpty(clazz.getSuperName()))
			return visiter.visitField(clazz, new CodeType(clazz, clazz.getSuperName()), name);

		return null;

	}

}
