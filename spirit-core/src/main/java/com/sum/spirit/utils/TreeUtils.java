package com.sum.spirit.utils;

import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

public class TreeUtils {

	public static int findStart(Statement statement, int index) {
		int start = -1;
		Token token = statement.getToken(index);
		for (int j = index - 1; j >= 0; j--) {
			Token lastToken = statement.getToken(j);
			if (lastToken.getTreeId() != null && lastToken.getTreeId().startsWith(token.getTreeId())) {
				start = j;
			} else {
				break;
			}
		}
		return start;
	}

	public static int findEnd(Statement statement, int index) {
		int end = -1;
		Token token = statement.getToken(index);
		for (int j = index + 1; j < statement.size(); j++) {
			Token nextToken = statement.getToken(j);
			if (nextToken.getTreeId() != null && nextToken.getTreeId().startsWith(token.getTreeId())) {
				end = j + 1;
			} else {
				break;
			}
		}
		return end;
	}

}
