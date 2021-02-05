package com.sum.spirit.core.element.lexer;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(-100)
public class BorderAction extends RegionAction {

//	@Override
//	public boolean isTrigger(LexerEvent event) {
//		return event.context.splitChars.contains(event.ch) && super.isTrigger(event);
//	}
//
//	@Override
//	public void doPushStack(LexerEvent event, List<Region> regions, String markName) {
//		LexerContext context = event.context;
//		StringBuilder builder = context.builder;
//		int shift = 0;
//		for (Region region : regions) {
//			if (region == null) {
//				continue;
//			}
//			if (context.splitChars.contains(builder.charAt(region.shift(shift).startIndex))) {
//				int lastShift = replaceStr(builder, region.getStartBorder(), "@separator" + context.nameCount++, context.replacedStrs);
//				shift += lastShift;
//				shift += replaceStr(builder, region.getEndBorder().shift(lastShift), "@separator" + context.nameCount++, context.replacedStrs);
//			}
//		}
//		context.splitChars.clear();
//	}
//
//	@Override
//	public void resetIndex(LexerEvent event) {
//		// ignore
//	}

}
