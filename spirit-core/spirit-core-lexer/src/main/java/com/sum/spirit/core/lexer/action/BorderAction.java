package com.sum.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.Lists;
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

		List<String> words = new ArrayList<>();
		Region mergedRegion = null;

		for (Region region : regions) {
			char startChar = builder.charAt(region.startIndex);
			char endChar = builder.charAt(region.endIndex - 1);
			// 如果开始字符和结束字符在指定的字符中，则对区域进行拆分
			if (splitChars.contains(startChar) && splitChars.contains(endChar)) {
				if (mergedRegion != null) {
					words.add(subRegion(builder, mergedRegion));
					mergedRegion = null;
				}
				words.addAll(splitRegion(builder, region));

			} else {
				mergedRegion = mergeRegions(Lists.toList(mergedRegion, region));
			}
		}

		if (mergedRegion != null) {
			words.add(subRegion(builder, mergedRegion));
		}

		// 添加到上下文参数中
		context.words = words;
		// 重置索引到结束位置
		context.index = builder.length();
	}

	@Override
	public void resetIndex(LexerEvent event) {
		// ignore
	}

}
