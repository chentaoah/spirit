package com.sum.spirit.java.utils;

import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

public class TreeUtils {

	public static int findStart(Statement statement, int index) {
		int start = -1;
		Token token = statement.getToken(index);
		String treeId = token.attr(AttributeEnum.TREE_ID);
		for (int j = index - 1; j >= 0; j--) {
			Token lastToken = statement.getToken(j);
			String lastTreeId = lastToken.attr(AttributeEnum.TREE_ID);
			if (lastTreeId != null && lastTreeId.startsWith(treeId)) {
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
		String treeId = token.attr(AttributeEnum.TREE_ID);
		for (int j = index + 1; j < statement.size(); j++) {
			Token nextToken = statement.getToken(j);
			String nextTreeId = nextToken.attr(AttributeEnum.TREE_ID);
			if (nextTreeId != null && nextTreeId.startsWith(treeId)) {
				end = j + 1;
			} else {
				break;
			}
		}
		return end;
	}

}
