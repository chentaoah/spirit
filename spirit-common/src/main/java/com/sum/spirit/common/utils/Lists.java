package com.sum.spirit.common.utils;

import java.util.ArrayList;
import java.util.List;

public class Lists {

	@SafeVarargs
	public static <T> List<T> toList(T... items) {
		if (items == null || items.length == 0) {
			return new ArrayList<T>();
		}
		List<T> list = new ArrayList<T>();
		for (T item : items) {
			if (item != null) {
				list.add(item);
			}
		}
		return list;
	}

	public static <T> int indexOf(List<T> list, int fromIndex, Matcher<T> matcher) {
		for (int index = fromIndex; index < list.size(); index++) {
			T item = list.get(index);
			if (matcher.accept(item)) {
				return index;
			}
		}
		return -1;
	}

	public static <T> int indexOf(List<T> list, Matcher<T> matcher) {
		return indexOf(list, 0, matcher);
	}

	public static <T> int lastIndexOf(List<T> list, Matcher<T> matcher) {
		int lastIndex = -1;
		for (int index = 0; index < list.size(); index++) {
			T item = list.get(index);
			if (matcher.accept(item)) {
				lastIndex = index > lastIndex ? index : lastIndex;
			}
		}
		return lastIndex;
	}

	public static interface Matcher<T> {
		boolean accept(T t);
	}

}
