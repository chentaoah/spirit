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
		char currChar = event.currChar;

		// 是否忽略该字符
		if (ignoreChars.contains(currChar) && context.currIndex > context.endIndex) {
			context.startIndex = -1;
			context.endIndex = LineUtils.findEndIndex(builder, context.currIndex, currChar, LineUtils.flipChar(currChar));
			ignoreChars.remove(new Character(currChar));
			return false;
		}

		// 是否已经到达结尾
		if (context.currIndex == builder.length() - 1) {
			return false;
		}

		// 如果是以下字符，则进行弹栈
		if (currChar == '"' || currChar == '\'' || currChar == '{' || currChar == '(' || currChar == '[') {
			return true;

		} else if (currChar == '<') {// 一般泛型声明都是以大写字母开头的
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
		char currChar = event.currChar;

		if (currChar == '"') {
			pushStack(builder, context.currIndex, '"', '"', "@str" + context.nameCount++, replacedStrs);

		} else if (currChar == '\'') {
			pushStack(builder, context.currIndex, '\'', '\'', "@char" + context.nameCount++, replacedStrs);

		} else if (currChar == '{') {
			pushStack(builder, context.currIndex, '{', '}', "@map" + context.nameCount++, replacedStrs);

		} else if (currChar == '(') {
			int idx = context.startIndex >= 0 ? context.startIndex : context.currIndex;
			pushStack(builder, idx, '(', ')', "@invoke_like" + context.nameCount++, replacedStrs);
			context.currIndex = idx;

		} else if (currChar == '[') {
			if (ignoreChars.contains('{') && context.currIndex > context.endIndex) {// 一般来说，Java中没有泛型数组的声明方式
				int idx = context.startIndex >= 0 ? context.startIndex : context.currIndex;
				pushStack(builder, idx, '[', ']', "@array_like" + context.nameCount++, replacedStrs);
				context.currIndex = idx;

			} else {
				int idx = context.startIndex >= 0 ? context.startIndex : context.currIndex;
				pushStack(builder, idx, '[', ']', '{', '}', "@array_like" + context.nameCount++, replacedStrs);
				context.currIndex = idx;
			}

		} else if (currChar == '<') {
			if (ignoreChars.contains('(') && context.currIndex > context.endIndex) {
				pushStack(builder, context.startIndex, '<', '>', "@generic" + context.nameCount++, replacedStrs);
				context.currIndex = context.startIndex;

			} else {
				pushStack(builder, context.startIndex, '<', '>', '(', ')', "@generic" + context.nameCount++, replacedStrs);
				context.currIndex = context.startIndex;
			}
		}
	}
}
