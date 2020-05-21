package com.sum.shy.core.utils;

import com.sum.shy.core.stmt.Stmt;
import com.sum.shy.core.stmt.Token;

public class TreeUtils {

	public static int findStart(Stmt stmt, int index) {
		int start = -1;
		Token token = stmt.getToken(index);
		for (int j = index - 1; j >= 0; j--) {
			Token lastToken = stmt.getToken(j);
			if (lastToken.getTreeId() != null && lastToken.getTreeId().startsWith(token.getTreeId())) {
				start = j;
			} else {
				break;
			}
		}
		return start;
	}

	public static int findEnd(Stmt stmt, int index) {
		int end = -1;
		Token token = stmt.getToken(index);
		for (int j = index + 1; j < stmt.size(); j++) {
			Token nextToken = stmt.getToken(j);
			if (nextToken.getTreeId() != null && nextToken.getTreeId().startsWith(token.getTreeId())) {
				end = j + 1;
			} else {
				break;
			}
		}
		return end;
	}

}
