package com.sum.spirit.java.convert;

import com.sum.pisces.api.annotation.Order;
import com.sum.spirit.api.convert.ElementConverter;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

@Order(-20)
public class SeparatorConverter implements ElementConverter {

	@Override
	public void convert(IClass clazz, Element element) {

		if (element.isIf() || element.isElseIf() || element.isFor() || element.isWhile() || element.isCatch() || element.isSync()) {
			insertBrackets(clazz, element.statement);
		}

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign() || element.isFieldAssign() || element.isInvoke() || element.isReturn()
				|| element.isSuper() || element.isThis() || element.isThrow() || element.isContinue() || element.isBreak()) {
			addLineEnd(clazz, element.statement);
		}
	}

	public void insertBrackets(IClass clazz, Statement statement) {
		// if text { // }catch Exception e{
		int index = findLastKeyword(statement);
		statement.tokens.add(index + 1, new Token(Constants.SEPARATOR_TOKEN, "("));
		if ("{".equals(statement.last())) {
			statement.tokens.add(statement.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")"));
		} else {
			statement.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ")"));
		}
	}

	public int findLastKeyword(Statement statement) {
		int index = -1;
		for (int i = 0; i < statement.size(); i++) {
			Token token = statement.getToken(i);
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

	public void addLineEnd(IClass clazz, Statement statement) {
		if (!"{".equals(statement.last()))
			statement.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ";"));
	}

}
