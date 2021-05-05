package com.sum.spirit.core.lexer;

import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CharsResult;

@Component
public class AliasCharsHandler extends AbstractCharsHandler {

	public String replace(String code, String alias, String className) {
		AliasCharsContext context = new AliasCharsContext();
		StringBuilder builder = new StringBuilder(code);
		context.builder = builder;
		context.alias = alias;
		context.className = className;
		CharsResult result = handle(context, builder);
		builder = (StringBuilder) result.payload;
		return builder.toString();
	}

	@Override
	public boolean isTrigger(CharEvent event) {
		AliasCharsContext context = (AliasCharsContext) event.context;
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
	public void handle(CharEvent event) {
		AliasCharsContext context = (AliasCharsContext) event.context;
		StringBuilder builder = context.builder;
		String alias = context.alias;
		String className = context.className;
		int endIndex = context.index + alias.length();
		if (endIndex <= builder.length()) {
			String text = builder.substring(context.index, endIndex);
			if (alias.equals(text)) {
				if (endIndex == builder.length() || !LineUtils.isLetter(builder.charAt(endIndex))) {
					builder.replace(context.index, endIndex, className);
				}
			}
		}
	}

	public static class AliasCharsContext extends CharsContext {
		public String alias;
		public String className;
	}
}
