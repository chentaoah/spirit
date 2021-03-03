package com.sum.spirit.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
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

	public static <T> void remove(List<T> list, int fromIndex, int toIndex) {
		for (int index = toIndex - 1; index >= fromIndex; index--) {
			list.remove(index);
		}
	}

	public static <T> T findOne(List<T> list, int fromIndex, int toIndex, Matcher<T> matcher) {
		int step = toIndex >= fromIndex ? 1 : -1;
		for (int index = fromIndex; index != toIndex; index += step) {
			T item = list.get(index);
			if (matcher.accept(item)) {
				return item;
			}
		}
		return null;
	}

	public static <T> List<T> filterUntilConditionNotMet(List<T> list, Filter<T> filter) {
		List<T> items = new ArrayList<>();
		Iterator<T> iterable = list.iterator();
		while (iterable.hasNext()) {
			T item = iterable.next();
			if (filter.accept(item)) {
				items.add(item);
				iterable.remove();
			} else {
				break;
			}
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public static <V, T> List<V> filterUntilConditionNotMet(List<T> list, Filter<T> filter, Factory<T> factory) {
		List<T> items = filterUntilConditionNotMet(list, filter);
		List<V> list0 = new ArrayList<>();
		items.forEach(item -> list0.add((V) factory.accept(item)));
		return list0;
	}

	public static <T> void visit(List<T> list, Visiter<T> visiter) {
		for (int index = 0; index < list.size(); index++) {
			visiter.accept(index, list.get(index));
		}
	}

	public static interface Matcher<T> {
		boolean accept(T t);
	}

	public static interface Filter<T> {
		boolean accept(T t);
	}

	public static interface Visiter<T> {
		void accept(int index, T t);
	}

	public static interface Factory<T> {
		Object accept(T t);
	}

}
