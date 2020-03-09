package com.sum.shy.java.processor;

public class PrintConverter {

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

}
