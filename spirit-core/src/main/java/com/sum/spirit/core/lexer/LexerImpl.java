package com.sum.spirit.core.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.sum.spirit.api.lexer.Lexer;
import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.utils.LineUtils;

@Component
public class LexerImpl implements Lexer {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	@Override
	public List<String> getWords(String text, Character... ignoreOnceChars) {

		// 拆分方法体时，会传入空的text
		if (StringUtils.isEmpty(text))
			return new ArrayList<>();

		List<Character> ignoreChars = new ArrayList<>(Arrays.asList(ignoreOnceChars));
		List<String> words = new ArrayList<>();
		Map<String, String> replacedStrs = new HashMap<>();
		StringBuilder builder = new StringBuilder(text.trim());

		// 1.整体替换
		// start 每个片段起始位置的游标
		// end 上一次忽略和跳过的结束位置的游标，为了保证最外层的被拆分，内部依然进行整体替换
		for (int index = 0, count = 0, start = -1, end = -1; index < builder.length(); index++) {

			char c = builder.charAt(index);

			// 如果该字符在集合中，那么则忽略一次，达到只拆分最外层符号的效果
			// index > end 其实是一种保护机制，让内部的字符，不被忽略
			if (ignoreChars.contains(c) && index > end) {
				start = -1;
				end = findEnd(builder, index, c, LineUtils.flipChar(c));
				ignoreChars.remove(new Character(c));
				continue;
			}

			// 判断是否接续字符，或者“.”字符，以即时更新起始索引
			if ((start < 0 && isContinueChar(c)) || c == '.')
				start = index;

			if (c == '"') {
				pushStack(builder, index, '"', '"', "@str" + count++, replacedStrs);

			} else if (c == '\'') {
				pushStack(builder, index, '\'', '\'', "@char" + count++, replacedStrs);

			} else if (c == '{') {
				pushStack(builder, index, '{', '}', "@map" + count++, replacedStrs);

			} else if (c == '(') {
				pushStack(builder, start >= 0 ? start : index, '(', ')', "@invoke_like" + count++, replacedStrs);
				index = start >= 0 ? start : index;

			} else if (c == '[') {
				// 一般来说，Java中没有泛型数组的声明方式
				if (ignoreChars.contains('{') && index > end) {
					pushStack(builder, start >= 0 ? start : index, '[', ']', "@array_like" + count++, replacedStrs);
					index = start >= 0 ? start : index;

				} else {
					pushStack(builder, start >= 0 ? start : index, '[', ']', '{', '}', "@array_like" + count++, replacedStrs);
					index = start >= 0 ? start : index;
				}

			} else if (c == '<') {
				if (start >= 0) {
					char d = builder.charAt(start);
					// 一般泛型声明都是以大写字母开头的
					if (d >= 'A' && d <= 'Z') {
						if (ignoreChars.contains('(') && index > end) {
							pushStack(builder, start, '<', '>', "@generic" + count++, replacedStrs);
							index = start;

						} else {
							pushStack(builder, start, '<', '>', '(', ')', "@generic" + count++, replacedStrs);
							index = start;
						}
					}
				}
			}

			if (!isContinueChar(c))
				start = -1;
		}

		text = builder.toString();

		// 2.添加空格，使拆分更简单
		for (SymbolEnum symbolEnum : SymbolEnum.SIGLE_SYMBOLS)
			text = text.replaceAll(symbolEnum.regex, " " + symbolEnum.value + " ");

		// 3.去掉多余的空格
		text = LineUtils.mergeSpaces(text);

		// 4.将被拆开的双字符符号中间的空格去掉
		for (SymbolEnum symbolEnum : SymbolEnum.DOUBLE_SYMBOLS)
			text = text.replaceAll(symbolEnum.badRegex, symbolEnum.value);

		// 5.通过空格进行拆分
		words = new ArrayList<>(Arrays.asList(text.split(" ")));

		// 6.如果一个片段中，包含“.”，那么进行更细致的拆分
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches() && !SemanticParser.isDouble(word)) {
				List<String> subWords = Arrays.asList(word.replaceAll("\\.", " .").split(" "));
				words.remove(i);
				words.addAll(i, subWords);
			}
		}

		// 7.将被替换的片段，重新替换回来
		for (int i = 0; i < words.size(); i++) {
			String str = replacedStrs.get(words.get(i));
			if (str != null)
				words.set(i, str);
		}

		return words;
	}

	public static boolean isContinueChar(char c) {
		return c == '@' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '.';
	}

	public static void pushStack(StringBuilder builder, int start, char left, char right, String markName, Map<String, String> replacedStrs) {
		int end = findEnd(builder, start, left, right);
		replaceStr(builder, start, end, markName, replacedStrs);
	}

	public static void pushStack(StringBuilder builder, int start, char left, char right, char left1, char right1, String markName,
			Map<String, String> replacedStrs) {
		int finalEnd = findEnd(builder, start, left, right);
		if (finalEnd != -1 && finalEnd + 1 < builder.length()) {
			char c = builder.charAt(finalEnd + 1);
			// 允许中间有个空格
			if (c == ' ' && finalEnd + 2 < builder.length()) {
				char d = builder.charAt(finalEnd + 2);
				if (d == left1) {
					int secondEnd = findEnd(builder, finalEnd + 2, left1, right1);
					if (secondEnd != -1)
						finalEnd = secondEnd;
				}
			} else {
				if (c == left1) {
					int secondEnd = findEnd(builder, finalEnd + 1, left1, right1);
					if (secondEnd != -1)
						finalEnd = secondEnd;
				}
			}
		}
		replaceStr(builder, start, finalEnd, markName, replacedStrs);
	}

	public static int findEnd(StringBuilder builder, int start, char left, char right) {
		boolean flag = false;
		for (int index = start, count = 0; index < builder.length(); index++) {
			char c = builder.charAt(index);
			// 如果是“"”符号，并且没有被转义
			if (c == '"' && LineUtils.isNotEscaped(builder.toString(), index))
				flag = !flag;
			if (!flag) {
				if (right == '"')
					return index;
				if (c == left) {
					count++;
				} else if (c == right) {
					count--;
					if (count == 0)
						return index;
				}
			}
		}
		return -1;
	}

	public static void replaceStr(StringBuilder builder, int start, int end, String markName, Map<String, String> replacedStrs) {
		if (end == -1)
			return;
		String content = builder.substring(start, end + 1);
		replacedStrs.put(markName, content);
		builder.replace(start, end + 1, " " + markName + " ");
	}

}
