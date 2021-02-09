package com.sum.spirit.core.element.lexer;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(-80)
public class RegionAction extends AbstractLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {

		LexerContext context = event.context;
		StringBuilder builder = context.builder;
		char ch = event.ch;

		// 是否已经到达结尾
		if (context.index == builder.length() - 1) {
			return false;
		}

		// 如果是以下字符，则进行弹栈
		if (ch == '"' || ch == '\'' || ch == '{' || ch == '(' || ch == '[') {
			return true;

		} else if (ch == '<') {// 一般泛型声明都是以大写字母开头的
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
	public void pushStack(LexerEvent event) {

		LexerContext context = event.context;
		StringBuilder builder = context.builder;
		char ch = event.ch;

		if (ch == '"') {
			Region region = findRegion(builder, context.index, '"', '"');
			doPushStack(event, Arrays.asList(region), "@str");

		} else if (ch == '\'') {
			Region region = findRegion(builder, context.index, '\'', '\'');
			doPushStack(event, Arrays.asList(region), "@char");

		} else if (ch == '{') {
			Region region = findRegion(builder, context.index, '{', '}');
			doPushStack(event, Arrays.asList(region), "@map");

		} else if (ch == '(') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = findRegion(builder, context.index, '(', ')');
			doPushStack(event, Arrays.asList(region0, region1), "@invoke_like");
			resetIndex(event);

		} else if (ch == '[') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = findRegion(builder, context.index, '[', ']');
			Region region2 = null;
			if (region1.endIndex < builder.length() && builder.charAt(region1.endIndex) == '{') {
				region2 = findRegion(builder, region1.endIndex, '{', '}');

			} else if (region1.endIndex + 1 < builder.length() && builder.charAt(region1.endIndex) == ' ' && builder.charAt(region1.endIndex + 1) == '{') {
				region2 = findRegion(builder, region1.endIndex + 1, '{', '}');
			}
			doPushStack(event, Arrays.asList(region0, region1, region2), "@array_like");
			resetIndex(event);

		} else if (ch == '<') {
			Region region0 = context.startIndex >= 0 ? new Region(context.startIndex, context.index) : null;
			Region region1 = findRegion(builder, context.index, '<', '>');
			Region region2 = null;
			if (region1.endIndex < builder.length() && builder.charAt(region1.endIndex) == '(') {
				region2 = findRegion(builder, region1.endIndex, '(', ')');
			}
			doPushStack(event, Arrays.asList(region0, region1, region2), "@generic");
			resetIndex(event);
		}
	}

	public void doPushStack(LexerEvent event, List<Region> regions, String markName) {
		LexerContext context = event.context;
		replaceStr(context.builder, mergeRegions((Region[]) regions.toArray()), markName + context.nameCount++, context.replacedStrs);
	}

	public void resetIndex(LexerEvent event) {
		LexerContext context = event.context;
		context.index = context.startIndex >= 0 ? context.startIndex : context.index;
	}

}
