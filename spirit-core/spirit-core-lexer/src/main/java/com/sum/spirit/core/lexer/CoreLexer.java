package com.sum.spirit.core.lexer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.action.BorderAction;
import com.sum.spirit.core.lexer.entity.CharsResult;
import com.sum.spirit.core.lexer.entity.LexerContext;

import cn.hutool.core.lang.Assert;

@Component
public class CoreLexer extends AbstractCursorLexer {

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getWords(String text) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		CharsResult result = handle(context, builder);
		return (List<String>) result.payload;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getSubWords(String text, Character... splitChars) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, BorderAction.PROFILE, splitChars);
		CharsResult result = handle(context, builder);
		Assert.notNull(result.payload, "Payload of result cannot be null!");
		List<String> words = new ArrayList<>();
		for (String word : (List<String>) result.payload) {
			if (word.length() == 1) {
				words.add(word);
			} else {
				words.addAll(getWords(word));
			}
		}
		return words;
	}

}
