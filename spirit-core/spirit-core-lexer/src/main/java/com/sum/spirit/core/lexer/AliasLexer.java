package com.sum.spirit.core.lexer;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerEvent;

@Component
@DependsOn("springUtils")
public class AliasLexer extends CoreLexer {

	public String replace(String code, String alias, String className) {
		AliasLexerContext context = new AliasLexerContext();
		context.builder = new StringBuilder(code);
		context.alias = alias;
		context.className = className;
		process(context, this);
		return context.builder.toString();
	}

	@Override
	public boolean isTrigger(LexerEvent event) {
		AliasLexerContext context = (AliasLexerContext) event.context;
		StringBuilder builder = context.builder;
		String alias = context.alias;
		char ch = event.ch;
		if (ch == '"') {
			context.index = LineUtils.findEndIndex(builder, context.index, '"', '"');
		} else if (ch == alias.charAt(0) && !LineUtils.isLetter(builder.charAt(context.index - 1))) {
			return true;
		}
		return false;
	}

	@Override
	public void pushStack(LexerEvent event) {
		AliasLexerContext context = (AliasLexerContext) event.context;
		StringBuilder builder = context.builder;
		String alias = context.alias;
		String className = context.className;
		// 获取结尾的索引
		int idx = context.index + alias.length();
		if (idx <= builder.length()) {
			String text = builder.substring(context.index, idx);
			if (alias.equals(text)) {
				if (!LineUtils.isLetter(builder.charAt(idx))) {
					builder.replace(context.index, idx, className);
				}
			}
		}
	}

	public static class AliasLexerContext extends LexerContext {
		public String alias;
		public String className;
	}
}
