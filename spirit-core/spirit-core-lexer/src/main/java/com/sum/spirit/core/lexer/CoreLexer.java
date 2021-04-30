package com.sum.spirit.core.lexer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.action.BorderAction;
import com.sum.spirit.core.lexer.entity.LexerContext;

import cn.hutool.core.lang.Assert;

@Component
public class CoreLexer extends AbstractCursorLexer {

	@Override
	public List<String> getWords(String text) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		handle(context, builder);
		return context.words;
	}

	@Override
	public List<String> getSubWords(String text, Character... splitChars) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, BorderAction.PROFILE, splitChars);
		handle(context, builder);
		Assert.notNull(context.words, "Words of context cannot be null!");
		List<String> words = new ArrayList<>();
		for (String word : context.words) {
			if (word.length() == 1) {
				words.add(word);
			} else {
				words.addAll(getWords(word));
			}
		}
		return words;
	}

}
