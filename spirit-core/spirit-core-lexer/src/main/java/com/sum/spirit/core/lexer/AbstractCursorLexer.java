package com.sum.spirit.core.lexer;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;

public abstract class AbstractCursorLexer extends AbstractLexer {

	@Override
	public void handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		char ch = event.ch;
		if ((context.startIndex < 0 && isContinuous(ch)) || isRefreshed(ch)) {
			context.startIndex = context.index;
		}
		super.handle(event);
		if (!isContinuous(ch)) {
			context.startIndex = -1;
		}
	}

	public boolean isContinuous(char ch) {
		return LineUtils.isLetter(ch) || ch == '@' || ch == '.';
	}

	public boolean isRefreshed(char ch) {
		return ch == '.';
	}

}
