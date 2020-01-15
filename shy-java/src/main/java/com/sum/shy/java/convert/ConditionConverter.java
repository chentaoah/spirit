package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.deduce.FastDerivator;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.type.api.Type;

public class ConditionConverter extends DefaultConverter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

		// if str { // if list.get(0) {
		Type type = FastDerivator.deriveStmt(clazz, stmt);
		if (type.isStr()) {
			JavaConverter.convert(clazz, stmt);
			stmt.tokens.add(1, new Token(Constants.CUSTOM_PREFIX_TOKEN, "StringUtils.isNotEmpty(", null));
			stmt.tokens.add(stmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")", null));
			clazz.addImport(StringUtils.class.getName());
		} else {
			JavaConverter.convert(clazz, stmt);
//			JavaConverter.convertEquals(clazz, stmt);// 这个比较特别，stmt的替换是通过处理Node实现的，其实是操作副本完成的
		}

		JavaConverter.insertBrackets(clazz, stmt);// 插入括号

		return stmt;
	}

}
