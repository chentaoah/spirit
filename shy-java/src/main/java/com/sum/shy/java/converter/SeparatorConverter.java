package com.sum.shy.java.converter;

import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.document.pojo.Element;
import com.sum.shy.document.pojo.Stmt;
import com.sum.shy.document.pojo.Token;
import com.sum.shy.pojo.Constants;

public class SeparatorConverter {

	public static void convert(IClass clazz, Element element) {

		if (element.isIf() || element.isElseIf() || element.isFor() || element.isWhile() || element.isCatch()
				|| element.isSync()) {
			insertBrackets(clazz, element.stmt);
		}

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign() || element.isFieldAssign()
				|| element.isInvoke() || element.isReturn() || element.isSuper() || element.isThis()
				|| element.isThrow() || element.isContinue() || element.isBreak()) {
			addLineEnd(clazz, element.stmt);
		}

	}

	public static void insertBrackets(IClass clazz, Stmt stmt) {
		// 第一个连续关键字之后，最后的分隔符之前
		int index = findKeyword(stmt);// if xxx { //}catch Exception e{
		stmt.tokens.add(index + 1, new Token(Constants.SEPARATOR_TOKEN, "("));
		if ("{".equals(stmt.last())) {
			stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")"));
		} else {
			stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ")"));
		}
	}

	public static int findKeyword(Stmt stmt) {
		int index = -1;
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isKeyword()) {
				index = i;
			} else {
				if (index == -1) {// 不是关键字的话,则进行下去
					continue;
				} else {// 如果不是关键字,但是关键字已经找到,则中断
					break;
				}
			}
		}
		return index;
	}

	public static void addLineEnd(IClass clazz, Stmt stmt) {
		if (!"{".equals(stmt.last()))
			stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ";"));// 这个添加的后缀,使得后面不会加上空格
	}

}
