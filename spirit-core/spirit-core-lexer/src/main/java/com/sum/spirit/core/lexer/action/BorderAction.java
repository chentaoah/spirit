package com.sum.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerEvent;
import com.sum.spirit.core.lexer.entity.Region;

@Component
@Order(-100)
public class BorderAction extends RegionAction {

	public static final String SUB_WORDS = "SUB_WORDS";

	@Override
	public void doPushStack(LexerEvent event, List<Region> regions, String markName) {
		LexerContext context = event.context;
		StringBuilder builder = context.builder;
		List<Character> splitChars = context.splitChars;
		Map<String, Object> attachments = context.attachments;
		List<String> subWords = new ArrayList<>();
		Region prefixRegion = null;
		for (Region region : regions) {
			if (region == null) {
				continue;
			}
			char startChar = builder.charAt(region.startIndex);
			char endChar = builder.charAt(region.endIndex - 1);
			if (splitChars.contains(startChar) && splitChars.contains(endChar)) {
				subWords.add(builder.substring(region.startIndex, region.startIndex + 1));
				if (region.endIndex - 1 > region.startIndex + 1) {
					String content = builder.substring(region.startIndex + 1, region.endIndex - 1);
					if (StringUtils.isNotBlank(content)) {
						subWords.add(content);
					}
				}
				subWords.add(builder.substring(region.endIndex - 1, region.endIndex));
			} else {
				prefixRegion = mergeRegions(prefixRegion, region);
			}
		}
		// 在头部插入前缀
		if (prefixRegion != null) {
			subWords.add(0, builder.substring(prefixRegion.startIndex, prefixRegion.endIndex));
		}
		// 添加到上下文参数中
		attachments.put(SUB_WORDS, subWords);
		// 重置索引到结束位置
		context.index = builder.length();
	}

	@Override
	public void resetIndex(LexerEvent event) {
		// ignore
	}

}
