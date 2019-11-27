package com.sum.shy.core.converter;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class FastAddConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		StringBuilder sb = new StringBuilder();
		Token var = stmt.getToken(0);// 变量
		Type type = var.getTypeAtt();
		if (type.isList()) {
			for (int i = 2; i < stmt.size(); i = i + 2) {
				sb.append(String.format("%s%s.add(%s);\n", i == 2 ? "" : indent, var.value, stmt.get(i)));
			}
		} else if (type.isMap()) {
			for (int i = 2; i < stmt.size(); i = i + 4) {
				sb.append(String.format("%s%s.put(%s);\n", i == 2 ? "" : indent, var.value, stmt.subStmt(i, i + 3)));
			}
		}
		// 删除最后一个换行符
		sb.deleteCharAt(sb.length() - 1);

		return new Stmt(sb.toString());
	}

}
