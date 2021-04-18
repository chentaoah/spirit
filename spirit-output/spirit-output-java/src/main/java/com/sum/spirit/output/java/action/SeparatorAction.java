package com.sum.spirit.output.java.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.annotation.Native;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.compile.action.AbstractElementAction;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

@Native
@Component
@Order(-20)
public class SeparatorAction extends AbstractElementAction {

	@Override
	public void handle(ElementEvent event) {
		IClass clazz = event.clazz;
		Element element = event.element;

		if (element.isIf() || element.isElseIf() || element.isWhile() || element.isCatch() || element.isSync()) {
			insertBrackets(clazz, element);
		}

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign() || //
				element.isFieldAssign() || element.isInvoke() || element.isReturn() || //
				element.isSuper() || element.isThis() || element.isThrow() || //
				element.isContinue() || element.isBreak()) {
			addLineEnd(clazz, element);
		}
	}

	public void insertBrackets(IClass clazz, Statement statement) {
		int keywordIndex = findLastKeyword(statement);
		int startIndex = keywordIndex >= 0 ? keywordIndex + 1 : -1;
		int endIndex = "{".equals(statement.last()) ? statement.size() - 1 : statement.size();
		if (endIndex != -1) {
			Statement subStatement = statement.subStmt(startIndex, endIndex);
			subStatement.add(0, new Token(TokenTypeEnum.SEPARATOR, "("));
			subStatement.add(new Token(TokenTypeEnum.SEPARATOR, ")"));
			Token subexpress = new Token(TokenTypeEnum.SUBEXPRESS, subStatement);
			statement.replaceTokens(startIndex, endIndex, subexpress);
		}
	}

	public int findLastKeyword(Statement statement) {
		int index = -1;
		for (int i = 0; i < statement.size(); i++) {
			Token token = statement.get(i);
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
		if (!"{".equals(statement.last())) {
			statement.add(new Token(TokenTypeEnum.SEPARATOR, ";"));
		}
	}

}
