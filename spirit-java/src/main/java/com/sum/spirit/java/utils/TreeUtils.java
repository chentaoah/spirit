package com.sum.spirit.java.utils;

import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.enums.AttributeEnum;

public class TreeUtils {

	public static int findStart(Statement statement, int index) {
		return findIndex(statement, index, -1);
	}

	public static int findEnd(Statement statement, int index) {
		return findIndex(statement, index, 1) + 1;
	}

	public static int findIndex(Statement statement, int index, int step) {
		int finalIndex = -1;
		String treeId = statement.getToken(index).attr(AttributeEnum.TREE_ID);
		for (int idx = index + step; idx >= 0 && idx < statement.size(); idx += step) {
			String nextTreeId = statement.getToken(idx).attr(AttributeEnum.TREE_ID);
			if (nextTreeId != null && nextTreeId.startsWith(treeId)) {
				finalIndex = idx;
			}
		}
		return finalIndex;
	}

}
