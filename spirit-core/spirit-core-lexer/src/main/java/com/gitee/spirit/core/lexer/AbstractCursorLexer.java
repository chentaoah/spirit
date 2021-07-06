package com.gitee.spirit.core.lexer;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.common.utils.LineUtils;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.LexerContext;

public abstract class AbstractCursorLexer extends AbstractLexer {

    @Override
    public Result handle(CharEvent event) {
        LexerContext context = (LexerContext) event.context;
        char ch = event.ch;
        if ((context.startIndex < 0 && isContinuous(ch)) || isRefreshed(ch)) {
            context.startIndex = context.index;
        }
        Result result = super.handle(event);
        if (!isContinuous(ch)) {
            context.startIndex = -1;
        }
        return result;
    }

    public boolean isContinuous(char ch) {
        return LineUtils.isLetter(ch) || ch == '@' || ch == '.' || ch == '$';
    }

    public boolean isRefreshed(char ch) {
        return ch == '.';
    }

}
