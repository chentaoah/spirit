package com.sum.shy.java.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.doc.Line;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;

public class PrintConverter implements Converter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

//		JavaConverter.convert(clazz, stmt);
//
//		Token token = stmt.getToken(0);
//		if (Constants.PRINT_KEYWORD.equals(token.toString())) {
//			stmt.tokens.set(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.info("));// 替换
//			stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");"));
//
//		} else if (Constants.DEBUG_KEYWORD.equals(token.toString())) {
//			stmt.tokens.set(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.debug("));// 替换
//			stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");"));
//
//		} else if (Constants.ERROR_KEYWORD.equals(token.toString())) {
//			stmt.tokens.set(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "logger.error("));// 替换
//			stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ");"));
//
//		}
		// 如果不存在
//		if (!clazz.existField("logger")) {
//			// 添加依赖
//			clazz.addImport(Logger.class.getName());
//			clazz.addImport(LoggerFactory.class.getName());
//			// 添加字段
//			Stmt fieldStmt = new Stmt("logger = LoggerFactory.getLogger(" + clazz.typeName + ".class)");
//			IField field = new IField(null, "static", new CodeType(clazz, "Logger"), "logger", fieldStmt);
//			clazz.staticFields.add(0, field);
//		}

		return stmt;
	}

}
