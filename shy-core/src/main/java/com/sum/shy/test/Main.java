package com.sum.shy.test;

import java.util.List;
import java.util.Map;
import com.sum.shy.library.StringUtils;
import com.sum.shy.library.Collection;
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
		List<String> list = Collection.newArrayList("hello", "yes");
		Map<String, String> map = Collection.newHashMap("key", "hi", "haha", list.get(0));
		for (String key : map.keySet()) {
			Object i = null;
		}
		for (int i = 0; i < 100; i++) {
			s = list.get(i);
			logger.info("thank {} very much!", "you");
		}
		return s;
	}

}
