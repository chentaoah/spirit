package com.sum.spirit.core;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.element.action.CoreLexer;
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
			StringBuilder builder = event.context.builder;
			AtomicInteger index = event.context.index;
			char c = event.c;
			if (c == '"') {
				index.set(LineUtils.findEndIndex(builder, index.get(), '"', '"'));
			} else if (c == alias.charAt(0) && !LineUtils.isLetter(builder.charAt(index.get() - 1))) {
				return true;
			}
			return false;
		}

		@Override
		public void pushStack(LexerEvent event) {
			StringBuilder builder = event.context.builder;
			AtomicInteger index = event.context.index;
			int idx = index.get() + alias.length();
			String text = builder.substring(index.get(), idx);
			if (alias.equals(text)) {
				if (idx < builder.length() && !LineUtils.isLetter(builder.charAt(idx))) {
					builder.replace(index.get(), idx, className);
				}
			}
		}
	}
}
