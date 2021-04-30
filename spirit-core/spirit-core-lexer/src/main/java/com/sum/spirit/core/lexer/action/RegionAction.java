package com.sum.spirit.core.lexer.action;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.TypeEnum;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.api.LexerAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerResult;
import com.sum.spirit.core.lexer.entity.LexerResult.State;
import com.sum.spirit.core.lexer.entity.Region;
import com.sum.spirit.core.lexer.utils.RegionUtils;

@Component
@Order(-80)
public class RegionAction implements LexerAction {

	@Override
	public boolean isTrigger(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;
		char ch = event.ch;

		if (context.index == builder.length() - 1) {
			return false;

		} else if (ch == '"' || ch == '\'' || ch == '{' || ch == '(' || ch == '[') {
			return true;

		} else if (ch == '<') {
			if (context.startIndex >= 0) {
				char d = builder.charAt(context.startIndex);
				if (d >= 'A' && d <= 'Z') {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public LexerResult handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;
		char ch = event.ch;

		if (ch == '"') {
			Region region = RegionUtils.findRegion(builder, context.index, '"', '"');
			return pushStack(event, ListUtils.toListNonNull(region));

		} else if (ch == '\'') {
			Region region = RegionUtils.findRegion(builder, context.index, '\'', '\'');
			return pushStack(event, ListUtils.toListNonNull(region));

		} else if (ch == '{') {
			Region region = RegionUtils.findRegion(builder, context.index, '{', '}');
			return pushStack(event, ListUtils.toListNonNull(region));

		} else if (ch == '(') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = RegionUtils.findRegion(builder, context.index, '(', ')');
			return pushStack(event, ListUtils.toListNonNull(region0, region1));

		} else if (ch == '[') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			if (region0 != null && !TypeEnum.isTypePrefix(RegionUtils.subRegion(builder, region0))) {
				region0 = null;
			}
			Region region1 = RegionUtils.findRegion(builder, context.index, '[', ']');
			Region region2 = null;
			if (region0 != null) {
				if (isCharAt(builder, region1.endIndex, '{')) {
					region2 = RegionUtils.findRegion(builder, region1.endIndex, '{', '}');

				} else if (isCharAt(builder, region1.endIndex, ' ') && isCharAt(builder, region1.endIndex + 1, '{')) {
					region2 = RegionUtils.findRegion(builder, region1.endIndex + 1, '{', '}');
				}
			}
			return pushStack(event, ListUtils.toListNonNull(region0, region1, region2));

		} else if (ch == '<') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = RegionUtils.findRegion(builder, context.index, '<', '>');
			Region region2 = isCharAt(builder, region1.endIndex, '(') ? RegionUtils.findRegion(builder, region1.endIndex, '(', ')') : null;
			return pushStack(event, ListUtils.toListNonNull(region0, region1, region2));
		}

		throw new RuntimeException("Can't handle the scene!");
	}

	public LexerResult pushStack(CharEvent event, List<Region> regions) {
		LexerContext context = (LexerContext) event.context;
		Region mergedRegion = RegionUtils.mergeRegions(regions);
		context.index = mergedRegion.endIndex - 1;
		return new LexerResult(State.SKIP, mergedRegion);
	}

	public boolean isCharAt(StringBuilder builder, int index, char ch) {
		return index < builder.length() && builder.charAt(index) == ch;
	}

}
