package com.sum.shy.core.lexical;

import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Token;
import com.sum.shy.utils.ArrayUtils;

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
	public static final String[] LINE_KEYWORDS = new String[] { "if", "while", "try", "sync", "return", "break",
			"continue", "throw", "print", "debug", "error" };

	/**
	 * 是否注解
	 * 
	 * @param words
	 * @return
	 */
	public static boolean isAnnotation(List<String> words) {
		return words != null && words.size() == 1 && words.get(0).startsWith("@");
	}

	/**
	 * 是否结构关键字
	 * 
	 * @param words
	 * @return
	 */
	public static boolean isStruct(List<String> words) {
		return words != null && words.size() > 0 && ArrayUtils.contain(SyntaxDefiner.STRUCT_KEYWORDS, words.get(0));
	}

	/**
	 * 获取结构体语法
	 * 
	 * @param words
	 * @return
	 */
	public static String getStructSyntax(List<String> words) {
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
		} catch (Exception e) {
			// ignore
		}
		// 未知
		return Constants.UNKNOWN;
	}

	/**
	 * 根据token来判断语法
	 * 
	 * @param tokens
	 * @return
	 */
	public static String getSyntax(List<Token> tokens) {
		try {
			// 空校验
			if (tokens == null || tokens.size() == 0)
				return Constants.UNKNOWN;

			// 第一个单词
			Token first = tokens.get(0);
			for (String keyword : LINE_KEYWORDS) {// 关键字语句
				if (keyword.equals(first.toString()))
					return keyword;
			}
			if (tokens.size() == 1 && "}".equals(first.toString())) {// 语句块的结束
				return Constants.END_SYNTAX;
			}
			if (tokens.size() == 1 && first.isInvokeLocal() && "super".equals(first.getMemberNameAtt())) {// 只有一个语素,并且以super开头
				return Constants.SUPER_SYNTAX;
			}
			if (tokens.size() == 1 && first.isInvokeLocal() && "this".equals(first.getMemberNameAtt())) {// 只有一个语素,并且以this开头
				return Constants.THIS_SYNTAX;
			}
			if (tokens.size() == 1 && first.isInvokeLocal()) {// 调用本地方法
				return Constants.INVOKE_SYNTAX;
			}
			if (tokens.size() == 1 && first.isNode() && first.getNode().token.isInvokeMethod()) {// 调用方法
				return Constants.INVOKE_SYNTAX;
			}

			// 第二个单词
			Token second = tokens.get(1);
			if (tokens.size() == 2 && first.isType() && second.isVar()) {// 如果是类型,则是类型说明语句
				return Constants.DECLARE_SYNTAX;
			}
			if (tokens.size() == 2 && first.isType() && second.isInvokeLocal()) {// 如果是类型,则是类型说明语句
				return Constants.FUNC_DECLARE_SYNTAX;
			}
			if ("=".equals(second.toString())) {// 字段定义或者赋值语句
				if (first.isVar()) {
					return Constants.ASSIGN_SYNTAX;
				} else if (first.isNode() && first.getNode().token.isVisitField()) {
					return Constants.FIELD_ASSIGN_SYNTAX;
				}
			}
			if ("<<".equals(second.toString())) {
				return Constants.FAST_ADD_SYNTAX;
			}
			if ("?".equals(second.toString())) {
				return Constants.JUDGE_INVOKE_SYNTAX;
			}

			// 第三个单词
			Token third = tokens.get(2);
			if ("for".equals(first.toString())) {
				if ("in".equals(third.toString())) {
					return Constants.FOR_IN_SYNTAX;
				}
				return Constants.FOR_SYNTAX;
			}
			if ("}".equals(first.toString())) {// else if语句
				if ("else".equals(second.toString())) {
					if ("if".equals(third.toString())) {
						return Constants.ELSEIF_SYNTAX;
					} else {
						return Constants.ELSE_SYNTAX;
					}
				} else if ("catch".equals(second.toString())) {
					return Constants.CATCH_SYNTAX;
				}
			}

		} catch (Exception e) {
			// ignore
		}

		// 未知
		return Constants.UNKNOWN;
	}

}
