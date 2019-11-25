package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

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

		// 声明语句,异常捕获,变量不进行追踪
		if (stmt.isDeclare() || stmt.isCatch())
			return;

		// 如果是for in语法,则只追踪后面的集合
		if (stmt.isForIn()) {
			// 只追踪第三个,也就是被遍历的集合
			findTokenType(clazz, method, block, line, stmt, 3);
			return;
		}

		// 直接遍历
		for (int i = 0; i < stmt.size(); i++) {
			findTokenType(clazz, method, block, line, stmt, i);
		}

	}

	private static void findTokenType(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt, int i) {
		Token token = stmt.getToken(i);
		if (token.isVar()) {
			try {
				getType(clazz, method, block, line, stmt, token, (String) token.value);
			} catch (Exception e) {
				if (stmt.isAssign() && i == 0) {// 赋值语句的第一个变量是可以容忍报错的
					token.setDeclaredAtt(false);// 设置为未被声明的
				} else {
					throw e;
				}
			}

		} else if (token.isInvokeMember()) {
			getType(clazz, method, block, line, stmt, token, token.getVarNameAtt());

		} else if (token.isMemberVar()) {
			getType(clazz, method, block, line, stmt, token, token.getVarNameAtt());

		} else if (token.isQuickIndex()) {
			getType(clazz, method, block, line, stmt, token, token.getVarNameAtt());

		}
		if (token.hasSubStmt()) {
			track(clazz, method, block, line, (Stmt) token.value);
		}

	}

	public static void getType(CtClass clazz, CtMethod method, String block, Line line, Stmt stmt, Token token,
			String name) {
		// 静态成员变量
		for (CtField field : clazz.staticFields) {
			if (field.name.equals(name)) {
				if (field.type == null)// 可能连锁推导时，字段还没有经过推导
					field.type = InvokeVisiter.visitElement(clazz, field);
				token.setTypeAtt(field.type);
				return;
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
		if (method != null) {
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (Param param : method.params) {
				if (param.name.equals(name)) {
					token.setTypeAtt(param.type);
					return;
				}
			}
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			Variable variable = method.findVariable(block, name);
			if (variable != null) {
				token.setTypeAtt(variable.type);
				return;
			}
		}

		throw new RuntimeException("Variable must be declared!number:[" + line.number + "], text:[ " + line.text.trim()
				+ " ], var:[" + name + "]");

	}

}
