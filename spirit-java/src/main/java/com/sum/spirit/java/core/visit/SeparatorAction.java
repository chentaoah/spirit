package com.sum.spirit.java.core.visit;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.c.visit.AbsElementAction;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

@Component
@Order(-20)
public class SeparatorAction extends AbsElementAction {

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Element element = event.element;

		if (element.isIf() || element.isElseIf() || element.isWhile() || element.isCatch() || element.isSync()) {
			insertBrackets(clazz, element.statement);
		}

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign() || element.isFieldAssign() || //
				element.isInvoke() || element.isReturn() || element.isSuper() || element.isThis() || //
				element.isThrow() || element.isContinue() || element.isBreak()) {
			addLineEnd(clazz, element.statement);
		}
	}

	public void insertBrackets(IClass clazz, Statement statement) {
		// if text {
		// }catch Exception e{
		int index = findLastKeyword(statement);
		statement.tokens.add(index + 1, new Token(TokenTypeEnum.SEPARATOR, "("));
		if ("{".equals(statement.last())) {
			statement.tokens.add(statement.size() - 1, new Token(TokenTypeEnum.SEPARATOR, ")"));
		} else {
			statement.tokens.add(new Token(TokenTypeEnum.SEPARATOR, ")"));
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
		if (!"{".equals(statement.last())) {
			statement.tokens.add(new Token(TokenTypeEnum.SEPARATOR, ";"));
		}
	}

}
