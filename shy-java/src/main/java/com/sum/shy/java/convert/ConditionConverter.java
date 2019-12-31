package com.sum.shy.java.convert;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.core.deduce.FastDerivator;
import com.sum.shy.entity.Constants;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.entity.Token;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.type.api.Type;

public class ConditionConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		// if str { // if list.get(0) {
		Type type = FastDerivator.deriveStmt(clazz, stmt);
		if (type.isStr()) {
			JavaConverter.convertCommon(clazz, stmt);
			stmt.tokens.add(1, new Token(Constants.PREFIX_TOKEN, "StringUtils.isNotEmpty(", null));
			stmt.tokens.add(stmt.size() - 1, new Token(Constants.SUFFIX_TOKEN, ")", null));
			clazz.addImport(StringUtils.class.getName());
		} else {
			JavaConverter.convertCommon(clazz, stmt);
			JavaConverter.convertEquals(clazz, stmt);// 这个比较特别，stmt的替换是通过处理Node实现的，其实是操作副本完成的
		}

		JavaConverter.insertBrackets(clazz, stmt);// 插入括号

		return stmt;
	}

}
