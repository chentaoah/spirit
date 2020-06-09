package com.sum.shy.api.service;

import java.util.List;

import com.sum.shy.api.StructRecognizer;
import com.sum.shy.common.Constants;
import com.sum.shy.common.KeywordTable;
import com.sum.shy.element.Token;

public class StructRecognizerImpl implements StructRecognizer {

	@Override
	public String getSyntax(List<Token> tokens) {
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
