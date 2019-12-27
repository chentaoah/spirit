package com.sum.shy.core.analyzer;

import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.utils.ArrayUtils;

/**
 * 语法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class SyntaxDefiner {
	// 结构体关键字,组成一个类的基本结构
	public static final String[] STRUCT_KEYWORDS = new String[] { "package", "import", "interface", "abstract", "class",
			"func" };
	// 关键字
	public static final String[] KEYWORDS = new String[] { "if", "while", "try", "sync", "return", "break", "continue",
			"throw", "print", "debug", "error" };

	public static String getSyntax(List<String> words) {
		try {
			// 空校验
			if (words == null || words.size() == 0)
				return Constants.UNKNOWN;

			// 第一个单词
			String first = words.get(0);
			for (String keyword : STRUCT_KEYWORDS) {// 关键字语句
				if (keyword.equals(first))
					return keyword;
			}
			for (String keyword : KEYWORDS) {// 关键字语句
				if (keyword.equals(first))
					return keyword;
			}
			if (words.size() == 1 && "}".equals(first)) {// 语句块的结束
				return Constants.END_SYNTAX;
			}
			if (words.size() == 1 && first.startsWith("@")) {// 注解
				return Constants.ANNOTATION_SYNTAX;
			}
			if (words.size() == 1 && first.startsWith("super(")) {// 只有一个语素,并且以super开头
				return Constants.SUPER_SYNTAX;
			}
			if (words.size() == 1 && first.startsWith("this(")) {// 只有一个语素,并且以this开头
				return Constants.THIS_SYNTAX;
			}
			if (words.size() == 1 && SemanticDelegate.isInvokeLocal(first)) {// 调用本地方法
				return Constants.INVOKE_SYNTAX;
			}

			// 第二个单词
			String second = words.get(1);
			if (words.size() == 2 && SemanticDelegate.isType(first) && SemanticDelegate.isVar(second)) {// 如果是类型,则是类型说明语句
				return Constants.DECLARE_SYNTAX;
			}
			if (words.size() == 2 && (SemanticDelegate.isVar(first) || SemanticDelegate.isType(first))
					&& SemanticDelegate.isInvokeMethod(first)) {// 调用方法
				return Constants.INVOKE_SYNTAX;
			}

			// 第三个单词
			String third = words.get(2);
			if ("for".equals(first)) {
				if ("in".equals(third)) {
					return Constants.FOR_IN_SYNTAX;
				}
				return Constants.FOR_SYNTAX;
			}
			if ("}".equals(first)) {// else if语句
				if ("else".equals(second)) {
					if ("if".equals(third)) {
						return Constants.ELSEIF_SYNTAX;
					} else {
						return Constants.ELSE_SYNTAX;
					}
				} else if ("catch".equals(second)) {
					return Constants.CATCH_SYNTAX;
				}
			}

			for (String word : words) {
				if ("=".equals(word)) {// 字段定义或者赋值语句
					return Constants.ASSIGN_SYNTAX;
				} else if ("<<".equals(word)) {
					return Constants.FAST_ADD_SYNTAX;
				} else if ("?".equals(word)) {
					return Constants.JUDGE_INVOKE_SYNTAX;
				}
			}

		} catch (Exception e) {
			// ignore
		}

		// 未知
		return Constants.UNKNOWN;
	}

	public static boolean isStruct(String keyword) {
		return ArrayUtils.contain(SyntaxDefiner.STRUCT_KEYWORDS, keyword);
	}

}
