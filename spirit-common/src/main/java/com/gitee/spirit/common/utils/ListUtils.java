package com.gitee.spirit.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

public class ListUtils {

	@SafeVarargs
	public static <T> List<T> asListNonNull(T... items) {
		if (items == null || items.length == 0) {
			return new ArrayList<T>();
		}
		List<T> list = new ArrayList<T>(items.length);
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

	public static <T> void removeByIndex(List<T> list, int fromIndex, int toIndex) {
		for (int index = toIndex - 1; index >= fromIndex; index--) {
			list.remove(index);
		}
	}

	public static <T> T remove(List<T> list, Matcher<T> matcher) {
		Iterator<T> iterable = list.iterator();
		while (iterable.hasNext()) {
			T item = iterable.next();
			if (matcher.accept(item)) {
				iterable.remove();
				return item;
			}
		}
		return null;
	}

	public static <T> T findOneByIndex(List<T> list, int fromIndex, int toIndex, Matcher<T> matcher) {
		int step = toIndex >= fromIndex ? 1 : -1;
		for (int index = fromIndex; index != toIndex; index += step) {
			T item = list.get(index);
			if (matcher.accept(item)) {
				return item;
			}
		}
		return null;
	}

	public static <T> T findOne(Iterable<T> collection, Matcher<T> matcher) {
		return CollUtil.findOne(collection, item -> matcher.accept(item));
	}

	public static <T> List<T> findAll(List<T> list, Matcher<T> matcher) {
		return CollUtil.filterNew(list, item -> matcher.accept(item));
	}

	public static <T> T findOneByScore(Iterable<T> collection, Selector<T> selector) {
		Integer maxScore = null;
		T finalItem = null;
		for (T item : collection) {
			Integer score = selector.accept(item);
			if (score != null) {
				Assert.isFalse(maxScore != null && maxScore.intValue() == score.intValue(), "The score cannot be the same!");
				if (maxScore == null || score > maxScore) {
					maxScore = score;
					finalItem = item;
				}
			}
		}
		return finalItem;
	}

	public static <T> List<T> filterStoppable(List<T> list, Matcher<T> matcher) {
		List<T> items = new ArrayList<>();
		Iterator<T> iterable = list.iterator();
		while (iterable.hasNext()) {
			T item = iterable.next();
			if (matcher.accept(item)) {
				items.add(item);
				iterable.remove();
			} else {
				break;
			}
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public static <V, T> List<V> filterStoppable(List<T> list, Matcher<T> matcher, Factory<T> factory) {
		List<T> items = filterStoppable(list, matcher);
		List<V> list0 = new ArrayList<>();
		items.forEach(item -> list0.add((V) factory.accept(item)));
		return list0;
	}

	public static <T> void visit(List<T> list, Visiter<T> visiter) {
		for (int index = 0; index < list.size(); index++) {
			visiter.accept(index, list.get(index));
		}
	}

	@SuppressWarnings("unchecked")
	public static <V, T> V collectOne(List<T> list, Factory<T> factory) {
		for (T item : list) {
			Object object = factory.accept(item);
			if (object != null) {
				return (V) object;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <V, T> V collectOne(List<T> list, Matcher<T> matcher, Factory<T> factory) {
		for (T item : list) {
			if (matcher.accept(item)) {
				return (V) factory.accept(item);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <V, T> List<V> collectAll(List<T> list, Matcher<T> matcher, Factory<T> factory) {
		List<V> list0 = new ArrayList<>();
		for (T item : list) {
			if (matcher.accept(item)) {
				list0.add((V) factory.accept(item));
			}
		}
		return list0;
	}

	public static interface Matcher<T> {
		boolean accept(T t);
	}

	public static interface Factory<T> {
		Object accept(T t);
	}

	public static interface Visiter<T> {
		void accept(int index, T t);
	}

	public static interface Selector<T> {
		Integer accept(T t);
	}

}
