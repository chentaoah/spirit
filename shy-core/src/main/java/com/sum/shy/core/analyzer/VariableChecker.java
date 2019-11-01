package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

/**
 * 调用校验器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年11月1日
 */
public class VariableChecker {

	public static void check(Clazz clazz, Method method, String block, Stmt stmt) {

		for (Token token : stmt.tokens) {
			if ("invoke_member".equals(token.type)) {
				String name = token.attachments.get("var_name");
				if (!isDeclared(clazz, method, block, name)) {
					throw new RuntimeException("Variable " + name + " must be declared in method " + method.name + "!");
				}
			} else if ("var_member".equals(token.type)) {
				String name = token.attachments.get("var_name");
				if (!isDeclared(clazz, method, block, name)) {
					throw new RuntimeException("Variable " + name + " must be declared in method " + method.name + "!");
				}
			} else if ("var".equals(token.type)) {
				String name = (String) token.value;
				if (!isDeclared(clazz, method, block, name)) {
					throw new RuntimeException("Variable " + name + " must be declared in method " + method.name + "!");
				}
			}
		}

	}

	public static boolean isDeclared(Clazz clazz, Method method, String block, String name) {
		// 静态成员变量
		for (Field field : clazz.staticFields) {
			if (field.name.equals(name)) {
				return true;
			}
		}
		// 成员变量
		for (Field field : clazz.fields) {
			if (field.name.equals(name)) {
				return true;
			}
		}
		// 如果在成员变量中没有声明,则查看方法内是否声明
		for (Param param : method.params) {
			if (param.name.equals(name)) {
				return true;
			}
		}
		// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
		Variable variable = method.findVariable(block, name);
		if (variable != null) {
			return true;
		}

		return false;

	}

}
