package com.sum.shy.api.service.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.api.Lexer;
import com.sum.shy.common.Symbol;
import com.sum.shy.common.SymbolTable;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.utils.LineUtils;

public class LexerImpl implements Lexer {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	@Override
	public List<String> getWords(String text) {

		// When parsing method content, empty content is passed in
		if (StringUtils.isEmpty(text))
			return new ArrayList<>();

		List<String> words = new ArrayList<>();
		Map<String, String> replacedStrs = new HashMap<>();
		StringBuilder builder = new StringBuilder(text.trim());

		// 1.overall replacement
		for (int index = 0, count = 0, start = -1; index < builder.length(); index++) {
			char c = builder.charAt(index);

			// determine whether to continue characters
			if ((start < 0 && isContinueChar(c)) || c == '.')
				start = index;

			if (c == '"') {
				push(builder, index, '"', '"', "@str" + count++, replacedStrs);

			} else if (c == '\'') {
				push(builder, index, '\'', '\'', "@char" + count++, replacedStrs);

			} else if (c == '{') {
				push(builder, index, '{', '}', "@map" + count++, replacedStrs);

			} else if (c == '(') {
				push(builder, start >= 0 ? start : index, '(', ')', "@invoke_like" + count++, replacedStrs);
				index = start >= 0 ? start : index;

			} else if (c == '[') {// Java generally does not declare generic arrays
				push(builder, start >= 0 ? start : index, '[', ']', '{', '}', "@array_like" + count++, replacedStrs);
				index = start >= 0 ? start : index;

			} else if (c == '<') {
				if (start >= 0) {
					char d = builder.charAt(start);
					if (d >= 'A' && d <= 'Z') {// generic types generally begin with a capital letter
						push(builder, start, '<', '>', '(', ')', "@generic" + count++, replacedStrs);
						index = start;
					}
				}
			}

			if (!isContinueChar(c))
				start = -1;
		}

		text = builder.toString();

		// 2.add space for easy split
		for (Symbol symbol : SymbolTable.SINGLE_SYMBOLS)
			text = text.replaceAll(symbol.regex, " " + symbol.value + " ");

		// 3.remove extra space
		text = LineUtils.mergeSpaces(text);

		// 4.correction of wrong symbols
		for (Symbol symbol : SymbolTable.DOUBLE_SYMBOLS)
			text = text.replaceAll(symbol.badRegex, symbol.value);

		// 5.split by ' '
		words = new ArrayList<>(Arrays.asList(text.split(" ")));

		// 6.continue splitting continuous access
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches() && !SemanticParserImpl.isDouble(word)) {
				List<String> subWords = new ArrayList<>(Arrays.asList(word.replaceAll("\\.", " .").split(" ")));
				words.remove(i);
				words.addAll(i, subWords);
			}
		}

		// 7.retrieve the replaced whole
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

	public static void push(StringBuilder builder, int start, char left, char right, String markName, Map<String, String> replacedStrs) {
		int end = findEnd(builder, start, left, right);
		replaceStr(builder, start, end, markName, replacedStrs);
	}

	public static void push(StringBuilder builder, int start, char left, char right, char left1, char right1, String markName,
			Map<String, String> replacedStrs) {
		int finalEnd = findEnd(builder, start, left, right);
		if (finalEnd != -1 && finalEnd + 1 < builder.length()) {
			char c = builder.charAt(finalEnd + 1);
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
			if (c == '"' && LineUtils.isBoundary(builder.toString(), index))
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
