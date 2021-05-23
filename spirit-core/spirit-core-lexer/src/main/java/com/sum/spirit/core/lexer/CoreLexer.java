package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.action.BorderAction;
import com.sum.spirit.core.lexer.entity.CommonResult;
import com.sum.spirit.core.lexer.entity.LexerContext;

import cn.hutool.core.lang.Assert;

@Component
public class CoreLexer extends AbstractCursorLexer {

	@Override
	public List<String> getWords(String text) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		CommonResult result = handle(context, builder);
		return result.get();
	}

	@Override
	public List<String> getSubWords(String text, Character... splitChars) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, BorderAction.BORDER_PROFILE, splitChars);
		CommonResult result = handle(context, builder);
		return result.get();
	}

}
