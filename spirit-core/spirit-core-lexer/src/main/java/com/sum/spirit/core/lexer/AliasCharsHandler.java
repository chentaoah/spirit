package com.sum.spirit.core.lexer;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;

@Component
@DependsOn("springUtils")
public class AliasCharsHandler extends AbstractCharsHandler {

	public String replace(String code, String alias, String className) {
		AliasCharsContext context = new AliasCharsContext();
		context.builder = new StringBuilder(code);
		context.alias = alias;
		context.className = className;
		handle(context, context.builder);
		return context.builder.toString();
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

	public static class AliasCharsContext extends CharsContext {
		public String alias;
		public String className;
	}
}
