package com.sum.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.Splitter;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerResult;
import com.sum.spirit.core.lexer.entity.Region;
import com.sum.spirit.core.lexer.entity.LexerResult.State;

@Component
@Order(-100)
public class BorderAction extends RegionAction {

	public static final String PROFILE = "BORDER_ACTION";

	@Override
	public boolean isTrigger(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		return PROFILE.equals(context.profile) && super.isTrigger(event);
	}

	@Override
	public LexerResult pushStack(CharEvent event, List<Region> regions) {
		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;
		List<Character> splitChars = context.splitChars;

		List<Integer> indexs = new ArrayList<>();
		for (Region region : regions) {
			char startChar = builder.charAt(region.startIndex);
			char endChar = builder.charAt(region.endIndex - 1);
			if (splitChars.contains(startChar) && splitChars.contains(endChar)) {
				indexs.add(region.startIndex);
				indexs.add(region.endIndex - 1);
			}
		}

		context.words = Splitter.splitByIndexsTrimRemain(builder.toString(), indexs);
		context.index = builder.length();
		return new LexerResult(State.BREAK, null);
	}

}
