package com.sum.slimx.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collection {

	@SuppressWarnings("unchecked")
	public static <T> T newArrayList(Object... objs) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < objs.length; i++)
			list.add(objs[i]);
		return (T) list;
	}

	@SuppressWarnings("unchecked")
	public static <T> T newHashMap(Object... objs) {
		Map<Object, Object> map = new HashMap<>();
		for (int i = 0; i < objs.length; i = i + 2)
			map.put(objs[i], objs[i + 1]);
		return (T) map;
	}

}
