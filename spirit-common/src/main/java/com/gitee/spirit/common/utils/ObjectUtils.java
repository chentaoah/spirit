package com.gitee.spirit.common.utils;

import java.util.Set;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.lang.Assert;

public class ObjectUtils {

	public static Set<Object> objects = new ConcurrentHashSet<>();

	public static void lock(Object obj) {
		Assert.isTrue(!objects.contains(obj), "Failed to acquire lock!");
		objects.add(obj);
	}

	public static void unlock(Object obj) {
		Assert.isTrue(objects.contains(obj), "Failed to release lock!");
		objects.remove(obj);
	}

}
