package com.sum.shy.java.convert;

import com.sum.shy.core.analyzer.FastDerivator;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.type.api.Type;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.lib.StringUtils;

public class ConditionConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		// if str { // if list.get(0) {
		Type type = FastDerivator.deriveStmt(clazz, stmt);
		if (type.isStr()) {
			JavaConverter.convertCommon(clazz, stmt);
			stmt.tokens.add(1, new Token(Constants.PREFIX_TOKEN, "StringUtils.isNotEmtpy(", null));
			stmt.tokens.add(stmt.size() - 1, new Token(Constants.SUFFIX_TOKEN, ")", null));
			clazz.addImport(StringUtils.class.getName());
		} else {
			JavaConverter.convertCommon(clazz, stmt);
			stmt = JavaConverter.convertEquals(clazz, stmt);// 这个比较特别，stmt的替换是通过处理Node实现的，其实是操作副本完成的
		}

		stmt.tokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "(", null));
		stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));

		return stmt;
	}

}
