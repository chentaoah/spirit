package com.sum.shy.test;

import com.sum.shy.library.StringUtils;
import com.sum.shy.library.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static Logger logger = LoggerFactory.getLogger(Main.class);

	public static String main(String[] args) {
		String s = "hello";
		if (StringUtils.equals(s, "hello")) {
		}
		if (!StringUtils.equals(s, "haha")) {
		}
		if (!StringUtils.isNotEmpty(s)) {
		} else {
		}
		if (StringUtils.isNotEmpty(s)) {
		}
		String y = "haha";
		while (StringUtils.isNotEmpty(s)) {
			y = "yes";
		}
		try {
		} catch (Exception e) {
		}
		List<Object> list = Collection.newArrayList("hello", 123);
		list.add("first");
		list.add("second");
		List<Map<String, String>> arr = Collection.newArrayList(Collection.newHashMap("key", "value", "key", "value"),
				Collection.newHashMap("key", "value"));
		Map<String, String> map = Collection.newHashMap("key", "hi", "haha", list.get(0));
		map.put("test", "test");
		map.put("hello", "hello");
		for (String key : map.keySet()) {
			Object i = null;
		}
		boolean b = list.get(100) != null;
		Map<Integer, Integer> map1234 = Collection.newHashMap(10, 1 == 2, 12 + 1000000000, "halo");
//		continue;
//		break;
		synchronized (list) {
		}
		boolean bbbb = list instanceof Object;
		if (list instanceof Object) {
		}
		try {
			throw new Exception("I am here!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Object key : list) {
			logger.info((String) list.get(10000));
		}
		if (list.size() > 0) {
			list.get(100);
			logger.info("hello world!");
		}
		if (list != null)
			list.get(100);
		int[] aaa = new int[10000];
		for (int ac : aaa) {
			logger.info("haha");
		}
		for (int i = 0; i < 100; i++) {

			logger.info("thank {} very much!", "you");
		}
		for (int i = 0; i < 100; i++) {
			logger.info("hello world!");
			String abc = "kucha";
		}
		return s;
	}

}
