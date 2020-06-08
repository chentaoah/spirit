package com.sum.shy.document.lexical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.core.pojo.Symbol;
import com.sum.shy.core.pojo.SymbolTable;
import com.sum.shy.core.utils.LineUtils;
import com.sum.shy.lib.StringUtils;

/**
 * 词法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class LexicalAnalyzer {

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	public static List<String> getWords(String text) {

		// getName()会传入空的content
		if (StringUtils.isEmpty(text))
			return new ArrayList<>();

		List<String> words = new ArrayList<>();
		Map<String, String> replacedStrs = new HashMap<>();
		StringBuilder builder = new StringBuilder(text.trim());

		// 1.整体替换
		for (int index = 0, count = 0, start = -1; index < builder.length(); index++) {
			char c = builder.charAt(index);
			// 如果是接续字符,则记录起始位置
			// .访问符会及时更新
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

			} else if (c == '[') {// 注意：不能声明泛型数组，并且带"{"和"}"，不能声明length
				push(builder, start >= 0 ? start : index, '[', ']', '{', '}', "@array_like" + count++, replacedStrs);
				index = start >= 0 ? start : index;

			} else if (c == '<') {// 泛型声明
				if (start >= 0) {// 必须有前缀
					char d = builder.charAt(start);
					if (d >= 'A' && d <= 'Z') {// 如果首字母是大写的话,才进行处理
						push(builder, start, '<', '>', '(', ')', "@generic" + count++, replacedStrs);
						index = start;
					}
				}
			}

			if (!isContinueChar(c))// 如果不是接续字符,则重置起始位置
				start = -1;
		}

		text = builder.toString();

		// 2.处理操作符,添加空格,方便后面的拆分
		for (Symbol symbol : SymbolTable.SINGLE_SYMBOLS)
			text = text.replaceAll(symbol.regex, " " + symbol.value + " ");

		// 3.将多余的空格去掉
		text = LineUtils.mergeSpaces(text);

		// 4.将那些被分离的符号,紧贴在一起
		for (Symbol symbol : SymbolTable.DOUBLE_SYMBOLS)
			text = text.replaceAll(symbol.badRegex, symbol.value);

		// 5.根据操作符,进行拆分
		words = new ArrayList<>(Arrays.asList(text.split(" ")));

		// 6.如果包含.但是又不是数字的话，则再拆一次
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches() && !SemanticDelegate.isDouble(word)) {
				List<String> subWords = new ArrayList<>(Arrays.asList(word.replaceAll("\\.", " .").split(" ")));
				words.remove(i);
				words.addAll(i, subWords);
			}
		}

		// 7.重新将替换的字符串替换回来
		for (int i = 0; i < words.size(); i++) {
			String str = replacedStrs.get(words.get(i));
			if (str != null)
				words.set(i, str);
		}

		return words;
	}

	public static boolean isContinueChar(char c) {// 是否接续字符
		return c == '@' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '.';
	}

	public static void push(StringBuilder builder, int start, char left, char right, String markName, Map<String, String> replacedStrs) {
		int end = findEnd(builder, start, left, right);
		replaceStr(builder, start, end, markName, replacedStrs);
	}

	public static void push(StringBuilder builder, int start, char left, char right, char left1, char right1, String markName, Map<String, String> replacedStrs) {
		int finalEnd = findEnd(builder, start, left, right);
		if (finalEnd != -1 && finalEnd + 1 < builder.length()) { // 判断后面的符号是否连续
			char c = builder.charAt(finalEnd + 1);
			if (c == ' ' && finalEnd + 2 < builder.length()) {// 继续往后延后一格
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
		boolean flag = false;// 是否进入"符号的范围内
		for (int index = start, count = 0; index < builder.length(); index++) {
			char c = builder.charAt(index);
			if (c == '"' && LineUtils.isBoundary(builder.toString(), index)) // 判断是否进入了字符串中
				flag = !flag;
			if (!flag) {
				if (right == '"')// 字符串是比较特殊的,含头不含尾,这里需要特殊处理
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
