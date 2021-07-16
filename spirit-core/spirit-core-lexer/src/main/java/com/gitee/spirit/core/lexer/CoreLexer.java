package com.gitee.spirit.core.lexer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.core.lexer.action.BorderAction;
import com.gitee.spirit.core.lexer.entity.LexerContext;

import cn.hutool.core.lang.Assert;

@Component
public class CoreLexer extends AbstractCursorLexer {

	@Override
	public List<String> getWords(String text) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder);
		Result result = handle(context, builder);
		return result.get();
	}

	@Override
	public List<String> getSubWords(String text, Character... splitChars) {
		Assert.notBlank(text, "Text cannot be blank!");
		StringBuilder builder = new StringBuilder(text.trim());
		LexerContext context = new LexerContext(builder, BorderAction.BORDER_PROFILE, splitChars);
		Result result = handle(context, builder);
		return result.get();
	}

}
