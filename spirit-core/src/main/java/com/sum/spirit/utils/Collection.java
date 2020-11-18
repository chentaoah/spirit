package com.sum.spirit.utils;

import java.util.HashMap;
import java.util.Map;

public class Collection {

	@SuppressWarnings("unchecked")
	public static <T> T newHashMap(Object... objs) {
		Map<Object, Object> map = new HashMap<>();
		for (int i = 0; i < objs.length; i = i + 2)
			map.put(objs[i], objs[i + 1]);
		return (T) map;
	}

}
