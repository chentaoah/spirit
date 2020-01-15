package com.sum.shy.java.convert;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;
import com.sum.shy.type.api.Type;

public class FastAddConverter implements Converter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

		JavaConverter.convert(clazz, stmt);// 转换

		StringBuilder sb = new StringBuilder();
		// list << a // map << key,value
		Token token = stmt.getToken(0);
		Type type = token.getTypeAtt();
		List<Stmt> subStmts = stmt.split("<<");
		if (type.isList()) {// 这一行自动加了缩进,后面的需要手动加上
			for (int i = 1; i < subStmts.size(); i++)
				sb.append(String.format("%s%s.add(%s);\n", i == 1 ? "" : indent, token.toString(), subStmts.get(i)));

		} else if (type.isMap()) {
			for (int i = 1; i < subStmts.size(); i++)
				sb.append(String.format("%s%s.put(%s);\n", i == 1 ? "" : indent, token.toString(), subStmts.get(i)));

		}

		sb.deleteCharAt(sb.length() - 1);// 删除最后一个换行符

		return new Stmt(sb.toString());
	}

}
