package com.sum.spirit.core.lexerx;

import java.util.Map;

import com.sum.spirit.utils.LineUtils;

public abstract class AbsLexerAction implements LexerAction {

	@Override
	public boolean isTrigger(char c) {
		return false;
	}

	public void pushStack(StringBuilder builder, int start, char left, char right, String markName, Map<String, String> replacedStrs) {
		int end = findEnd(builder, start, left, right);
		replaceStr(builder, start, end, markName, replacedStrs);
	}

	public void pushStack(StringBuilder builder, int start, char left, char right, char left1, char right1, String markName, Map<String, String> replacedStrs) {
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

	public int findEnd(StringBuilder builder, int start, char left, char right) {
		boolean flag = false;
		for (int index = start, count = 0; index < builder.length(); index++) {
			char c = builder.charAt(index);
			if (c == '"' && LineUtils.isNotEscaped(builder.toString(), index))// 如果是“"”符号，并且没有被转义
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

	public void replaceStr(StringBuilder builder, int start, int end, String markName, Map<String, String> replacedStrs) {
		if (end == -1)
			return;
		String content = builder.substring(start, end + 1);
		replacedStrs.put(markName, content);
		builder.replace(start, end + 1, " " + markName + " ");
	}

}
