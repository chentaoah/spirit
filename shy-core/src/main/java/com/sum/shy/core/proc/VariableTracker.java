package com.sum.shy.core.proc;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IParameter;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;
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

	public static void trackStmt(IClass clazz, MethodContext context, Stmt stmt) {
		for (Token token : stmt.tokens) {
			if (token.hasSubStmt())
				trackStmt(clazz, context, token.getSubStmt());

			if (token.isVar()) {
				String name = token.toString();
				IType type = findType(clazz, context, name);
				token.setTypeAtt(type);

			} else if (token.isArrayIndex()) {
				String name = token.getMemberNameAtt();
				IType type = findType(clazz, context, name);
				token.setTypeAtt(type);
			}

		}

	}

	public static IType findType(IClass clazz, MethodContext context, String name) {

		// super引用,指向的是父类
		if (Constants.SUPER_KEYWORD.equals(name))
			return new CodeType(clazz, clazz.getSuperName());// 这里可能是比较隐晦的逻辑，因为

		// this引用，指向的是这个类本身
		if (Constants.THIS_KEYWORD.equals(name))
			return new CodeType(clazz, clazz.getClassName(), clazz.getTypeName());// 这里可能是比较隐晦的逻辑，因为

		// 先在方法上下文中找
		IMethod method = context.method;
		if (method != null) {
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			for (Variable variable : context.variables) {
				if (variable.blockId.equals(context.blockId) && variable.name.equals(name))
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
			if (field.getName().equals(name)) {
				if (field.type == null)
					field.type = MemberVisiter.visitMember(clazz, field);
				return field.type;
			}
		}

		// 从继承里面去找
		if (StringUtils.isNotEmpty(clazz.getSuperName())) {
			String className = clazz.findImport(clazz.getSuperName());
			IClass father = Context.get().findClass(className);
			return findType(father, null, name);
		}

		throw new RuntimeException("Variable must be declared!number:[" + context.line.number + "], text:[ "
				+ context.line.text.trim() + " ], var:[" + name + "]");

	}

}
