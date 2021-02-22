package com.sum.spirit.java.visiter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.compile.action.AbstractElementAction;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

@Component
@Order(-20)
public class SeparatorAction extends AbstractElementAction {

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Element element = event.element;

		if (element.isIf() || element.isElseIf() || element.isWhile() || element.isCatch() || element.isSync()) {
			insertBrackets(clazz, element);
		}

		if (element.isDeclare() || element.isDeclareAssign() || element.isAssign() || element.isFieldAssign() || //
				element.isInvoke() || element.isReturn() || element.isSuper() || element.isThis() || //
				element.isThrow() || element.isContinue() || element.isBreak()) {
			addLineEnd(clazz, element);
		}
	}

	public void insertBrackets(IClass clazz, Statement statement) {
		// if text {
		// }catch Exception e{
		int index = findLastKeyword(statement);
		statement.add(index + 1, new Token(TokenTypeEnum.SEPARATOR, "("));
		if ("{".equals(statement.last())) {
			statement.add(statement.size() - 1, new Token(TokenTypeEnum.SEPARATOR, ")"));
		} else {
			statement.add(new Token(TokenTypeEnum.SEPARATOR, ")"));
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
