package com.sum.spirit.core.lexer;

import java.util.Map;

import com.sum.spirit.api.LexerAction;
import com.sum.spirit.utils.LineUtils;

public abstract class AbstractLexerAction implements LexerAction {

	public void pushStack(StringBuilder builder, int start, char left, char right, String markName, Map<String, String> replacedStrs) {
		int end = LineUtils.findEndFromIndex(builder, start, left, right);
		replaceStr(builder, start, end + 1, markName, replacedStrs);
	}

	public void pushStack(StringBuilder builder, int start, char left, char right, char left1, char right1, String markName, Map<String, String> replacedStrs) {
		int finalEnd = LineUtils.findEndFromIndex(builder, start, left, right);
		if (finalEnd != -1 && finalEnd + 1 < builder.length()) {
			char c = builder.charAt(finalEnd + 1);
			if (c == ' ' && finalEnd + 2 < builder.length()) {// 允许中间有个空格
				char d = builder.charAt(finalEnd + 2);
				if (d == left1) {
					int secondEnd = LineUtils.findEndFromIndex(builder, finalEnd + 2, left1, right1);
					if (secondEnd != -1) {
						finalEnd = secondEnd;
					}
				}
			} else {
				if (c == left1) {
					int secondEnd = LineUtils.findEndFromIndex(builder, finalEnd + 1, left1, right1);
					if (secondEnd != -1) {
						finalEnd = secondEnd;
					}
				}
			}
		}
		replaceStr(builder, start, finalEnd + 1, markName, replacedStrs);
	}

	public void replaceStr(StringBuilder builder, int start, int end, String markName, Map<String, String> replacedStrs) {
		if (end == -1) {
			return;
		}
		String content = builder.substring(start, end);
		if (replacedStrs != null) {
			replacedStrs.put(markName, content);
		}
		builder.replace(start, end, " " + markName + " ");
	}

}
