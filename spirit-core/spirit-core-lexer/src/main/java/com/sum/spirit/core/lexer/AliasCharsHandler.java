package com.sum.spirit.core.lexer;

import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CharsResult;

@Component
public class AliasCharsHandler extends AbstractCharsHandler {

	/**
	 * -将别名替换为完整的类名
	 * 
	 * @param code
	 * @param alias
	 * @param className
	 * @return
	 */
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

	/**
	 * -遇到字符串，则跳过，如果当前字符和别名的别名的首字母相同，则触发处理逻辑
	 */
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

	/**
	 * -根据alias的宽度，截取一段字符串，如果相等，则进行替换 -注意：这段字符串的前后，不能是连续的字符
	 */
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
