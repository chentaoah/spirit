package com.sum.spirit.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

public class Splitter {

	public static List<String> splitByIndexsTrimRemain(String str, List<Integer> indexs) {
		Assert.isTrue(indexs != null && indexs.size() > 0, "indexs can not be empty!");
		List<String> strs = new ArrayList<>();
		int lastIndex = 0;
		for (Integer index : indexs) {
			if (index >= lastIndex) {
				if (index > lastIndex) {
					addIfNotBlank(strs, str.substring(lastIndex, index));
				}
				addIfNotBlank(strs, str.substring(index, index + 1));
			}
			lastIndex = index + 1;
		}
		if (lastIndex != str.length()) {
			addIfNotBlank(strs, str.substring(lastIndex, str.length()));
		}
		return strs;
	}

	public static void addIfNotBlank(List<String> strs, String str) {
		if (StringUtils.isNotBlank(str)) {
			strs.add(str);
		}
	}

	public static <T> List<List<T>> splitByMatcherTrim(List<T> list, Matcher<T> matcher) {
		int[] indexs = CollUtil.indexOfAll(list, item -> matcher.accept(item));
		List<List<T>> list0 = new ArrayList<>();
		int lastIndex = 0;
		for (Integer index : indexs) {
			if (index > lastIndex) {
				list0.add(new ArrayList<>(list.subList(lastIndex, index)));
			}
			lastIndex = index + 1;
		}
		if (lastIndex != list.size()) {
			list0.add(new ArrayList<>(list.subList(lastIndex, list.size())));
		}
		return list0;
	}

	@SuppressWarnings("unchecked")
	public static <V, T> List<V> splitByMatcherTrim(List<T> list, Matcher<T> matcher, Factory<List<T>> factory) {
		List<List<T>> items = splitByMatcherTrim(list, matcher);
		List<V> list0 = new ArrayList<>();
		items.forEach(item -> list0.add((V) factory.accept(item)));
		return list0;
	}

	public static interface Matcher<T> {
		boolean accept(T t);
	}

	public static interface Factory<T> {
		Object accept(T t);
	}

}
