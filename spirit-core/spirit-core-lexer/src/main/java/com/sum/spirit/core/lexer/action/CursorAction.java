package com.sum.spirit.core.lexer.action;

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

	@Override
	public void handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		char ch = event.ch;
		// 是否连续字符
		if ((context.startIndex < 0 && isContinuous(ch)) || isRefreshed(ch)) {
			context.startIndex = context.index;
		}
		// 真正处理的逻辑
		if (action.isTrigger(event)) {
			action.handle(event);
		}
		// 如果不是连续字符，则重置游标
		if (!isContinuous(ch)) {
			context.startIndex = -1;
		}
	}

	public boolean isContinuous(char ch) {// 是否连续
		return ch == '@' || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_' || ch == '.';
	}

	public boolean isRefreshed(char ch) {// 是否需要刷新
		return ch == '.';
	}

}
