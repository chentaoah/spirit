package com.sum.shy.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collection {

	@SuppressWarnings("unchecked")
	public static <T> T newArrayList(Object... objects) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < objects.length; i++) {
			list.add(objects[i]);
		}
		return (T) list;
	}

	@SuppressWarnings("unchecked")
	public static <T> T newHashMap(Object... objects) {
		Map<Object, Object> map = new HashMap<>();
		for (int i = 0; i + 2 < objects.length; i = i + 2) {
			map.put(objects[i], objects[i + 1]);
		}
		return (T) map;
	}

}
