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

	@Override
	public void doPushStack(CharEvent event, List<Region> regions, String markName) {

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

		// 添加到上下文参数中
		context.words = Splitter.splitByIndexsTrimRemain(builder.toString(), indexs);
		// 重置索引到结束位置
		context.index = builder.length();
	}

	@Override
	public void resetIndex(CharEvent event) {
		// ignore
	}

}
