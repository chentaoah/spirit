package com.gitee.spirit.stdlib;

import java.util.Collection;
import java.util.Map;

public class Emptys {

	public static boolean empty(String str) {
		return str == null || str.length() == 0;
	}

	public static <T> boolean empty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <K, V> boolean empty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}

}
