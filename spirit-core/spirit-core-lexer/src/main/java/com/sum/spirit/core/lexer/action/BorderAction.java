package com.sum.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.Splitter;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerEvent;
import com.sum.spirit.core.lexer.entity.Region;

@Component
@Order(-100)
public class BorderAction extends RegionAction {

	@Override
	public void doPushStack(LexerEvent event, List<Region> regions, String markName) {

		LexerContext context = event.context;
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
	public void resetIndex(LexerEvent event) {
		// ignore
	}

}
