package com.sum.spirit.core.element.lexer;

import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.utils.LineUtils;

@Component
@Order(-100)
public class RegionAction extends AbstractLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {

		LexerContext context = event.context;
		StringBuilder builder = context.builder;
		List<Character> ignoreChars = context.ignoreChars;
		char char0 = event.char0;

		// 是否忽略该字符
		if (ignoreChars.contains(char0) && context.index > context.endIndex) {
			context.startIndex = -1;
			context.endIndex = LineUtils.findEndIndex(builder, context.index, char0, LineUtils.flipChar(char0));
			ignoreChars.remove(new Character(char0));
			return false;
		}

		// 是否已经到达结尾
		if (context.index == builder.length() - 1) {
			return false;
		}

		// 如果是以下字符，则进行弹栈
		if (char0 == '"' || char0 == '\'' || char0 == '{' || char0 == '(' || char0 == '[') {
			return true;

		} else if (char0 == '<') {// 一般泛型声明都是以大写字母开头的
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
		List<Character> ignoreChars = context.ignoreChars;
		Map<String, String> replacedStrs = context.replacedStrs;
		char char0 = event.char0;

		if (char0 == '"') {
			pushStack(builder, context.index, '"', '"', "@str" + context.nameCount++, replacedStrs);

		} else if (char0 == '\'') {
			pushStack(builder, context.index, '\'', '\'', "@char" + context.nameCount++, replacedStrs);

		} else if (char0 == '{') {
			pushStack(builder, context.index, '{', '}', "@map" + context.nameCount++, replacedStrs);

		} else if (char0 == '(') {
			int idx = context.startIndex >= 0 ? context.startIndex : context.index;
			pushStack(builder, idx, '(', ')', "@invoke_like" + context.nameCount++, replacedStrs);
			context.index = idx;

		} else if (char0 == '[') {
			if (ignoreChars.contains('{') && context.index > context.endIndex) {// 一般来说，Java中没有泛型数组的声明方式
				int idx = context.startIndex >= 0 ? context.startIndex : context.index;
				pushStack(builder, idx, '[', ']', "@array_like" + context.nameCount++, replacedStrs);
				context.index = idx;

			} else {
				int idx = context.startIndex >= 0 ? context.startIndex : context.index;
				pushStack(builder, idx, '[', ']', '{', '}', "@array_like" + context.nameCount++, replacedStrs);
				context.index = idx;
			}

		} else if (char0 == '<') {
			if (ignoreChars.contains('(') && context.index > context.endIndex) {
				pushStack(builder, context.startIndex, '<', '>', "@generic" + context.nameCount++, replacedStrs);
				context.index = context.startIndex;

			} else {
				pushStack(builder, context.startIndex, '<', '>', '(', ')', "@generic" + context.nameCount++, replacedStrs);
				context.index = context.startIndex;
			}
		}
	}
}
