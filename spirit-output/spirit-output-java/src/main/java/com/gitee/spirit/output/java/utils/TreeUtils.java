package com.gitee.spirit.output.java.utils;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.element.entity.Statement;

public class TreeUtils {

	public static int findIndex(Statement statement, int index, int step) {
		int finalIndex = -1;
		String treeId = statement.get(index).attr(Attribute.TREE_ID);
		for (int idx = index + step; idx >= 0 && idx < statement.size(); idx += step) {
			String nextTreeId = statement.get(idx).attr(Attribute.TREE_ID);
			if (nextTreeId != null && nextTreeId.startsWith(treeId)) {
				finalIndex = idx;
			}
		}
		return finalIndex;
	}

	public static int findStart(Statement statement, int index) {
		return findIndex(statement, index, -1);
	}

	public static int findEnd(Statement statement, int index) {
		int finalIndex = findIndex(statement, index, 1);
		return finalIndex >= 0 ? finalIndex + 1 : finalIndex;
	}

}
