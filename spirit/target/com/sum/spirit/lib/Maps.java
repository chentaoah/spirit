package com.sum.spirit.lib;

import java.util.HashMap;
import java.util.Map;

public class Maps {

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<>();
	}

	public static <K, V> Map<K, V> of() {
		return newHashMap();
	}

	public static <K, V> Map<K, V> of(K k1, V v1) {
		Map<K, V> map = of();
		map.put(k1, v1);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
		Map<K, V> map = of();
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> map = of();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> map = of();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		Map<K, V> map = of();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
		Map<K, V> map = of();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
		Map<K, V> map = of();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		map.put(k7, v7);
		return map;
	}

}
