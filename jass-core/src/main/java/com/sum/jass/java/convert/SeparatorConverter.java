package com.sum.jass.java.convert;

import com.sum.pisces.api.annotation.Order;
import com.sum.jass.api.convert.ElementConverter;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.common.Constants;
import com.sum.jass.pojo.element.Element;
import com.sum.jass.pojo.element.Statement;
import com.sum.jass.pojo.element.Token;

@Order(-20)
public class SeparatorConverter implements ElementConverter {

	@Override
	public void convert(IClass clazz, Element element) {

		if (element.isIf() || element.isElseIf() || element.isFor() || element.isWhile() || element.isCatch() || element.isSync()) {
			insertBrackets(clazz, element.stmt);
		}

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign() || element.isFieldAssign() || element.isInvoke() || element.isReturn()
				|| element.isSuper() || element.isThis() || element.isThrow() || element.isContinue() || element.isBreak()) {
			addLineEnd(clazz, element.stmt);
		}
	}

	public void insertBrackets(IClass clazz, Statement stmt) {
		// if text { // }catch Exception e{
		int index = findLastKeyword(stmt);
		stmt.tokens.add(index + 1, new Token(Constants.SEPARATOR_TOKEN, "("));
		if ("{".equals(stmt.last())) {
			stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")"));
		} else {
			stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ")"));
		}
	}

	public int findLastKeyword(Statement stmt) {
		int index = -1;
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isKeyword()) {
				index = i;
			} else {
				if (index == -1) {
					continue;
				} else {
					break;
				}
			}
		}
		return index;
	}

	public void addLineEnd(IClass clazz, Statement stmt) {
		if (!"{".equals(stmt.last()))
			stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ";"));
	}

}
