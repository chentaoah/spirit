package com.sum.spirit.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeadVisiter<T> {

	public List<T> visit(List<T> list, Filter<T> filter) {
		List<T> items = new ArrayList<>();
		Iterator<T> iterable = list.iterator();
		while (iterable.hasNext()) {
			T item = iterable.next();
			boolean accepted = filter.accept(item);
			if (accepted) {
				items.add(item);
				iterable.remove();
			} else {
				break;
			}
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public <V> List<V> visit(List<T> list, Filter<T> filter, Factory<T> factory) {
		List<T> items = visit(list, filter);
		List<V> list0 = new ArrayList<>();
		items.forEach(item -> list0.add((V) factory.accept(item)));
		return list0;
	}

	public static interface Filter<T> {
		boolean accept(T t);
	}

	public static interface Factory<T> {
		Object accept(T t);
	}

}
