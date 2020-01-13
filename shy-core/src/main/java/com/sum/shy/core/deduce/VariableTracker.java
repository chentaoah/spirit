package com.sum.shy.core.deduce;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtField;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.clazz.Param;
import com.sum.shy.clazz.Variable;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.api.Type;

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

	public static void trackStmt(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt) {
		for (Token token : stmt.tokens)
			findType(clazz, method, block, line, stmt, token);
	}

	public static void findType(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt, Token token) {

		if (token.hasSubStmt())
			trackStmt(clazz, method, block, line, token.getSubStmt());
		if (token.isNode())
			trackStmt(clazz, method, block, line, token.toNode().toStmt());

		if (token.isVar()) {
			String name = token.toString();
			Type type = findType(clazz, method, block, name);
			checkType(line, name, type);
			token.setTypeAtt(type);

		} else if (token.isArrayIndex()) {
			String name = token.getMemberNameAtt();
			Type type = findType(clazz, method, block, name);
			checkType(line, name, type);
			token.setTypeAtt(type);

		}

	}

	public static Type findType(CtClass clazz, CtMethod method, String block, String name) {
		// this引用，指向的是这个类本身
		if ("this".equals(name))
			return new CodeType(clazz, clazz.getClassName(), clazz.typeName);// 这里可能是比较隐晦的逻辑，因为

		// super引用,指向的是父类
		if ("super".equals(name))
			return new CodeType(clazz, clazz.superName);// 这里可能是比较隐晦的逻辑，因为

		// 先在最近的位置找变量
		if (method != null) {
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			Variable variable = method.findVariable(block, name);
			if (variable != null)
				return variable.type;
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (Param param : method.params) {
				if (param.name.equals(name))
					return param.type;
			}
		}
		// 成员变量
		for (CtField field : clazz.fields) {
			if (field.name.equals(name)) {
				if (field.type == null)
					field.type = InvokeVisiter.visitMember(clazz, field);
				return field.type;
			}
		}
		// 静态成员变量
		for (CtField field : clazz.staticFields) {
			if (field.name.equals(name)) {
				if (field.type == null)// 可能连锁推导时，字段还没有经过推导
					field.type = InvokeVisiter.visitMember(clazz, field);
				return field.type;
			}
		}
		// 从继承里面去找
		if (StringUtils.isNotEmpty(clazz.superName))
			return InvokeVisiter.visitField(clazz, new CodeType(clazz, clazz.superName), name);

		return null;

	}

	private static void checkType(Line line, String name, Type type) {
		if (type == null)
			throw new RuntimeException("Variable must be declared!number:[" + line.number + "], text:[ "
					+ line.text.trim() + " ], var:[" + name + "]");
	}

}
