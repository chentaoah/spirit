package com.sum.shy.java.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.type.CodeType;

public class PrintConverter extends DefaultConverter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

		JavaConverter.convertCommon(clazz, stmt);

		Token token = stmt.getToken(0);
		if ("print".equals(token.value)) {
			stmt.tokens.set(0, new Token(Constants.PREFIX_TOKEN, "logger.info(", null));// 替换
			stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");", null));

		} else if ("debug".equals(token.value)) {
			stmt.tokens.set(0, new Token(Constants.PREFIX_TOKEN, "logger.debug(", null));// 替换
			stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");", null));

		} else if ("error".equals(token.value)) {
			stmt.tokens.set(0, new Token(Constants.PREFIX_TOKEN, "logger.error(", null));// 替换
			stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");", null));

		}
		// 如果不存在
		if (!clazz.existField("logger")) {
			// 添加依赖
			clazz.addImport(Logger.class.getName());
			clazz.addImport(LoggerFactory.class.getName());
			// 添加字段
			clazz.addField(new IField(null, "static", new CodeType(clazz, "Logger"), "logger",
					new Stmt("logger = LoggerFactory.getLogger(" + clazz.typeName + ".class)")));
		}

		return stmt;
	}

}
