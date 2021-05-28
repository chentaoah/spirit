package com.gitee.spirit.core.lexer.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CommonResult;
import com.gitee.spirit.core.lexer.entity.CommonState;
import com.gitee.spirit.core.lexer.entity.LexerContext;
import com.gitee.spirit.core.lexer.entity.Region;

@Component
@Order(-100)
public class BorderAction extends RegionAction {

	public static final String BORDER_PROFILE = "BORDER_PROFILE";

	@Override
	public boolean isTrigger(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		return BORDER_PROFILE.equals(context.profile) && super.isTrigger(event);
	}

	@Override
	public CommonResult pushStack(CharEvent event, List<Region> regions) {
		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;
		List<Character> splitChars = context.splitChars;

		List<Region> borderRegions = new ArrayList<>();
		for (Region region : regions) {
			char startChar = builder.charAt(region.startIndex);
			char endChar = builder.charAt(region.endIndex - 1);
			if (splitChars.contains(startChar) && splitChars.contains(endChar)) {
				borderRegions.add(new Region(region.startIndex, region.startIndex + 1));
				borderRegions.add(new Region(region.endIndex - 1, region.endIndex));
			}
		}

		context.profile = null;
		if (!borderRegions.isEmpty()) {
			int borderIndex = borderRegions.get(0).startIndex;
			context.index = borderIndex;
			if (borderIndex > 0) {
				borderRegions.add(0, new Region(0, borderIndex));
			}
		}
		return new CommonResult(CommonState.SKIP, borderRegions);
	}

}
