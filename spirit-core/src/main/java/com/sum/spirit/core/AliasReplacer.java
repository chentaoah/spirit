package com.sum.spirit.core;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.element.action.CoreLexer;
import com.sum.spirit.core.element.lexer.LexerContext;
import com.sum.spirit.core.element.lexer.LexerEvent;
import com.sum.spirit.utils.LineUtils;

@Component
public class AliasReplacer {

	public String replace(IClass clazz, String code) {
		for (Import imp : clazz.getAliasImports()) {
			code = replace(code, imp.getAlias(), imp.getClassName());
		}
		return code;
	}

	public String replace(String code, String alias, String className) {
		StringBuilder builder = new StringBuilder(code);
		AliasLexer lexer = new AliasLexer(alias, className);
		lexer.replace(builder);
		return builder.toString();
	}

	public static class AliasLexer extends CoreLexer {

		public String alias;
		public String className;

		public AliasLexer(String alias, String className) {
			this.alias = alias;
			this.className = className;
		}

		@Override
		public boolean isTrigger(LexerEvent event) {
			LexerContext context = event.context;
			StringBuilder builder = context.builder;
			char char0 = event.char0;
			if (char0 == '"') {
				context.index = LineUtils.findEndIndex(builder, context.index, '"', '"');
			} else if (char0 == alias.charAt(0) && !LineUtils.isLetter(builder.charAt(context.index - 1))) {
				return true;
			}
			return false;
		}

		@Override
		public void pushStack(LexerEvent event) {
			LexerContext context = event.context;
			StringBuilder builder = context.builder;
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
	}
}
