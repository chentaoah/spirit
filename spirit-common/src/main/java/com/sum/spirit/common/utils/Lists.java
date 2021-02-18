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

}
