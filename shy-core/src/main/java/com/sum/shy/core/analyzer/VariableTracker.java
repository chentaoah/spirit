package com.sum.shy.core.analyzer;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;
import com.sum.shy.library.Collection;
import com.sum.shy.library.StringUtils;

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

	public static void track(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt) {
		// 直接遍历
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			try {
				findType(clazz, method, block, line, stmt, token);

			} catch (Exception e) {
				if (stmt.isAssign() && i == 0) {
					token.setDeclaredAtt(false);
				} else {
					throw e;
				}
			}
		}
	}

	public static void findType(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt, Token token) {

		if (token.isVar()) {
			findVariableType(clazz, method, block, line, stmt, token, (String) token.value);

		} else if (token.isInvokeMember()) {
			findVariableType(clazz, method, block, line, stmt, token, token.getVarNameAtt());

		} else if (token.isMemberVar()) {
			findVariableType(clazz, method, block, line, stmt, token, token.getVarNameAtt());

		} else if (token.isQuickIndex()) {
			findVariableType(clazz, method, block, line, stmt, token, token.getVarNameAtt());

		}
		if (token.hasSubStmt()) {
			track(clazz, method, block, line, (Stmt) token.value);
		}

	}

	public static void findVariableType(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt, Token token,
			String name) {
		// this引用，指向的是这个类本身
		if ("this".equals(name)) {
			// 这里可能是比较隐晦的逻辑，因为
			token.setTypeAtt(new CodeType(clazz, clazz.getClassName(), clazz.typeName));
			return;
		}
		// super引用,指向的是父类
		if ("super".equals(name)) {
			// 这里可能是比较隐晦的逻辑，因为
			token.setTypeAtt(new CodeType(clazz, clazz.superName));
			return;
		}

		// 先在最近的位置找变量
		if (method != null) {
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			Variable variable = method.findVariable(block, name);
			if (variable != null) {
				token.setTypeAtt(variable.type);
				return;
			}
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (Param param : method.params) {
				if (param.name.equals(name)) {
					token.setTypeAtt(param.type);
					return;
				}
			}
		}
		// 成员变量
		for (CtField field : clazz.fields) {
			if (field.name.equals(name)) {
				if (field.type == null)
					field.type = InvokeVisiter.visitElement(clazz, field);
				token.setTypeAtt(field.type);
				return;
			}
		}
		// 静态成员变量
		for (CtField field : clazz.staticFields) {
			if (field.name.equals(name)) {
				if (field.type == null)// 可能连锁推导时，字段还没有经过推导
					field.type = InvokeVisiter.visitElement(clazz, field);
				token.setTypeAtt(field.type);
				return;
			}
		}

		// 从继承里面去找
		if (StringUtils.isNotEmpty(clazz.superName)) {
			Type type = InvokeVisiter.getReturnType(clazz, new CodeType(clazz, clazz.superName),
					Collection.newArrayList(name), null, null);
			token.setTypeAtt(type);
			return;
		}

		throw new RuntimeException("Variable must be declared!number:[" + line.number + "], text:[ " + line.text.trim()
				+ " ], var:[" + name + "]");

	}

}
