package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.deduce.FastDerivator;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.type.api.IType;

public class ConditionConverter implements Converter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

		// 如果只有三个元素,并且中间这个元素的类型为字符串
		boolean flag = false;
		if (stmt.size() == 3) {// if str { // if list.get(0) {
			IType type = FastDerivator.deriveStmt(clazz, stmt);
			if (type.isStr())
				flag = true;
		}

		JavaConverter.convert(clazz, stmt);// 转换

		if (flag) {
			stmt.tokens.add(1, new Token(Constants.CUSTOM_PREFIX_TOKEN, "StringUtils.isNotEmpty("));
			stmt.tokens.add(stmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
			clazz.addImport(StringUtils.class.getName());
		}

		JavaConverter.insertBrackets(clazz, stmt);// 插入括号

		return stmt;
	}

}
