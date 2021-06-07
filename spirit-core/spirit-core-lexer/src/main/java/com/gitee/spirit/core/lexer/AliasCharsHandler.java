package com.gitee.spirit.core.lexer;

import org.springframework.stereotype.Component;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.common.utils.LineUtils;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CharsContext;

@Component
public class AliasCharsHandler extends AbstractCharsHandler {

	public String replace(String code, String alias, String className) {
		AliasCharsContext context = new AliasCharsContext();
		StringBuilder builder = new StringBuilder(code);
		context.builder = builder;
		context.alias = alias;
		context.className = className;
		Result result = handle(context, builder);
		builder = (StringBuilder) result.data;
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
	public Result handle(CharEvent event) {
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
		return null;
	}

	public static class AliasCharsContext extends CharsContext {
		public String alias;
		public String className;
	}
}
