package com.sum.shy.java;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.Import;

public class AliasReplacer {

	public static String replace(IClass clazz, String code) {
		for (Import import1 : clazz.imports) {
			if (import1.hasAlias()) {
				code = replace(code, import1.getAlias(), import1.getClassName());
			}
		}
		return code;
	}

	private static String replace(String code, String alias, String className) {
		return null;
	}

	public static List<String> getStrs(String code) {
		return null;
	}

	public static class Pair {
		public int start;
		public int end;

		public Pair(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	public static void main(String[] args) {

	}

}
