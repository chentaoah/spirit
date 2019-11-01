package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

public class AssignmentConverter extends AbstractConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<String> lines,
			int index, String line, Stmt stmt) {
//		System.out.println(line);
		// 如果是单纯的变量,而不是成员变量,则需要进行类型声明
		String tokenType = stmt.getToken(0).type;
		if ("var".equals(tokenType)) {
			// 获取变量名
			String name = stmt.get(0);
			// 判断是否在成员变量中声明
			boolean flag = false;
			for (Field field : clazz.fields) {
				if (field.name.equals(name)) {
					flag = true;
				}
			}
			// 如果在成员变量中没有声明,则查看方法内是否声明
			for (Param param : method.params) {
				if (param.name.equals(name)) {
					flag = true;
				}
			}
			if (!flag) {
				// 如果成员变量和方法声明中都没有声明该变量,则从变量追踪器里查询
				Variable variable = method.findVariable(block, name);
				if (variable == null) {
					// 追加变量
					method.addVariable(new Variable(block, name));
					// 如果没有,则在最前面追加类型
					String type = TypeDerivator.getType(stmt);
					List<String> genericTypes = TypeDerivator.getGenericTypes(stmt);
					String str = convertType(type, genericTypes);
					stmt.tokens.add(0, new Token("type", str, null));

				}
			}
		}
		return super.convert(sb, block, indent, clazz, method, lines, index, line, stmt);

	}

}
