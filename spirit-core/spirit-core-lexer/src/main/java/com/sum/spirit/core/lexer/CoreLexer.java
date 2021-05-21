package com.sum.spirit.core.lexer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.action.BorderAction;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CharsResult;
import com.sum.spirit.core.lexer.entity.LexerContext;

import cn.hutool.core.lang.Assert;

@Component
public class CoreLexer extends AbstractCursorLexer {

	@Override
	public List<String> getWords(String text) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		CharsResult result = handle(context, builder);
		return result.get();
	}

	@Override
	public List<String> getSubWords(String text, Character... splitChars) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, BorderAction.PROFILE, splitChars);
		CharsResult result = handle(context, builder);
		return result.get();
	}

	@Override
	public CharsResult buildResult(CharsContext context, StringBuilder builder) {
		LexerContext lexerContext = (LexerContext) context;
		CharsResult result = super.buildResult(context, builder);
		if (BorderAction.PROFILE.equals(lexerContext.profile)) {
			List<String> finalWords = new ArrayList<>();
			List<String> words = result.get();
			for (String word : words) {
				if (word.length() == 1) {
					finalWords.add(word);
				} else {
					finalWords.addAll(getWords(word));
				}
			}
			return new CharsResult(finalWords);
		}
		return result;
	}

}
