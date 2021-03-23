package com.sum.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.Splitter;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.Region;

@Component
public class BorderAction extends RegionAction {

	/**
	 * -该类对弹栈行为进行了一定改造，主要对分隔符进行拆分，并将索引直接置为结束
	 */
	@Override
	public void pushStack(CharEvent event, List<Region> regions, String markName) {

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
	}

	@Override
	public void resetIndex(CharEvent event) {
		// ignore
	}

}
