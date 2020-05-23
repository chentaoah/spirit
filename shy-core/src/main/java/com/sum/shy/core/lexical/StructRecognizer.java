package com.sum.shy.core.lexical;

import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.KeywordTable;
import com.sum.shy.core.stmt.Token;

/**
 * 语法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class StructRecognizer {

	public static String getSyntax(List<Token> tokens) {
		Token first = tokens.get(0);
		// 关键字语句
		if (KeywordTable.isStruct(first.toString()))
			return first.toString();
		// 语句结束
		if (tokens.size() == 1 && "}".equals(first.toString()))
			return Constants.END_SYNTAX;
		// 注解
		if (tokens.size() == 1 && first.isAnnotation())
			return Constants.ANNOTATION_SYNTAX;
		// 未知
		return null;
	}

}
