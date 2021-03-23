package com.sum.spirit.core.lexer.action;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.TypeEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.Region;

@Component
@Order(-100)
public class RegionAction extends AbstractLexerAction {

	/**
	 * -到达结尾，则直接返回false
	 * -泛型前缀一般都以大写字母开始
	 */
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

	/**
	 * str[0] => str [0], String[0] => String[0], String[]{"str", "str"} => String[]{"str", "str"}
	 */
	@Override
	public void handle(CharEvent event) {

		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;
		char ch = event.ch;

		if (ch == '"') {
			Region region = findRegion(builder, context.index, '"', '"');
			pushStack(event, Lists.toListNonNull(region), "@str");

		} else if (ch == '\'') {
			Region region = findRegion(builder, context.index, '\'', '\'');
			pushStack(event, Lists.toListNonNull(region), "@char");

		} else if (ch == '{') {
			Region region = findRegion(builder, context.index, '{', '}');
			pushStack(event, Lists.toListNonNull(region), "@map");

		} else if (ch == '(') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = findRegion(builder, context.index, '(', ')');
			pushStack(event, Lists.toListNonNull(region0, region1), "@invoke_like");
			resetIndex(event);

		} else if (ch == '[') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			if (region0 != null && !TypeEnum.isTypePrefix(subRegion(builder, region0))) {
				region0 = null;
			}
			Region region1 = findRegion(builder, context.index, '[', ']');
			Region region2 = null;
			if (region0 != null) {
				if (isCharAt(builder, region1.endIndex, '{')) {
					region2 = findRegion(builder, region1.endIndex, '{', '}');

				} else if (isCharAt(builder, region1.endIndex, ' ') && isCharAt(builder, region1.endIndex + 1, '{')) {
					region2 = findRegion(builder, region1.endIndex + 1, '{', '}');
				}
			}
			pushStack(event, Lists.toListNonNull(region0, region1, region2), "@array_like");
			resetIndex(event);

		} else if (ch == '<') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = findRegion(builder, context.index, '<', '>');
			Region region2 = isCharAt(builder, region1.endIndex, '(') ? findRegion(builder, region1.endIndex, '(', ')') : null;
			pushStack(event, Lists.toListNonNull(region0, region1, region2), "@generic");
			resetIndex(event);
		}
	}

	public boolean isCharAt(StringBuilder builder, int index, char ch) {
		return index < builder.length() && builder.charAt(index) == ch;
	}

	public void pushStack(CharEvent event, List<Region> regions, String markName) {
		LexerContext context = (LexerContext) event.context;
		replaceRegion(context.builder, mergeRegions(regions), markName + context.nameCount++, context.replacedStrs);
	}

	public void resetIndex(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		context.index = context.startIndex >= 0 ? context.startIndex : context.index;
	}

}
