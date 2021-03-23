package com.sum.spirit.core.lexer.action;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;

public class CursorAction implements CharAction {

	public CharAction action;

	public CursorAction(CharAction action) {
		this.action = action;
	}

	@Override
	public boolean isTrigger(CharEvent event) {
		return true;
	}

	/**
	 * -接续字符和刷新字符，是游标敏感的，用以标记区域的起始位置
	 * -先进行弹栈，再刷新startIndex为-1，例如“[”这种情况
	 */
	@Override
	public void handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		char ch = event.ch;
		if ((context.startIndex < 0 && isContinuous(ch)) || isRefreshed(ch)) {
			context.startIndex = context.index;
		}
		if (action.isTrigger(event)) {
			action.handle(event);
		}
		if (!isContinuous(ch)) {
			context.startIndex = -1;
		}
	}

	/**
	 * -是否接续字符
	 * 
	 * @param ch
	 * @return
	 */
	public boolean isContinuous(char ch) {
		return LineUtils.isLetter(ch) || ch == '@' || ch == '.';
	}

	/**
	 * -是否刷新字符
	 * 
	 * @param ch
	 * @return
	 */
	public boolean isRefreshed(char ch) {
		return ch == '.';
	}

}
