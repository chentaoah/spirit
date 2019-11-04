package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
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

	public static void check(Clazz clazz, Method method, String block, Line line, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isVar()) {
				String type = getType(clazz, method, block, line, stmt, i, (String) token.value);
				if (type != null)
					token.setTypeAttachment(type);
			} else if (token.isInvokeMember()) {
				getType(clazz, method, block, line, stmt, i, token.getVarNameAttachment());// 只校验
			} else if (token.isMemberVar()) {
				getType(clazz, method, block, line, stmt, i, token.getVarNameAttachment());// 只校验
			}
			if (token.hasSubStmt()) {
				check(clazz, method, block, line, (Stmt) token.value);
			}

		}

	}

	public static String getType(Clazz clazz, Method method, String block, Line line, Stmt stmt, int index,
			String name) {
		// 静态成员变量
		for (Field field : clazz.staticFields) {
			if (field.name.equals(name)) {
				return field.type;
			}
		}
		// 成员变量
		for (Field field : clazz.fields) {
			if (field.name.equals(name)) {
				return field.type;
			}
		}
		if (method != null) {
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (Param param : method.params) {
				if (param.name.equals(name)) {
					return param.type;
				}
			}
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			Variable variable = method.findVariable(block, name);
			if (variable != null) {
				return variable.type;
			}
		}
		// 如果是赋值语句，并且是第一个，可以不用校验
		if (stmt.isAssignment() && index == 0) {
			return null;
		}

		throw new RuntimeException("Variable must be declared!line:[" + line + "],var:[" + name + "]");

	}

}
