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
				try {
					getType(clazz, method, block, line, stmt, token, (String) token.value);
				} catch (Exception e) {
					// 赋值语句的第一个变量是可以容忍报错的
					if (stmt.isAssignment() && i == 0) {
//						String type = TypeDerivator.getType(stmt);
//						List<String> genericTypes = TypeDerivator.getGenericTypes(stmt);
//						token.setTypeAttachment(type);
//						token.setGenericTypesAttachment(genericTypes);
					} else {
						throw e;
					}
				}

			} else if (token.isInvokeMember()) {
				getType(clazz, method, block, line, stmt, token, token.getVarNameAttachment());// 只校验

			} else if (token.isMemberVar()) {
				getType(clazz, method, block, line, stmt, token, token.getVarNameAttachment());// 只校验

			}
			if (token.hasSubStmt()) {
				check(clazz, method, block, line, (Stmt) token.value);
			}

		}

	}

	public static void getType(Clazz clazz, Method method, String block, Line line, Stmt stmt, Token token,
			String name) {
		// 静态成员变量
		for (Field field : clazz.staticFields) {
			if (field.name.equals(name)) {
				token.setTypeAttachment(field.type);
				token.setGenericTypesAttachment(field.genericTypes);
				return;
			}
		}
		// 成员变量
		for (Field field : clazz.fields) {
			if (field.name.equals(name)) {
				token.setTypeAttachment(field.type);
				token.setGenericTypesAttachment(field.genericTypes);
				return;
			}
		}
		if (method != null) {
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (Param param : method.params) {
				if (param.name.equals(name)) {
					token.setTypeAttachment(param.type);
					token.setGenericTypesAttachment(param.genericTypes);
					return;
				}
			}
			// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
			Variable variable = method.findVariable(block, name);
			if (variable != null) {
				token.setTypeAttachment(variable.type);
				token.setGenericTypesAttachment(variable.genericTypes);
				return;
			}
		}

		throw new RuntimeException("Variable must be declared!number:[" + line.number + "], text:[ " + line.text.trim()
				+ " ], var:[" + name + "]");

	}

}