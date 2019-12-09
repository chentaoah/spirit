package com.sum.test.block;

import com.sum.shy.library.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sum.shy.library.Collection;
import java.util.Map;
import java.util.List;

public class Block {

	public static Logger logger = LoggerFactory.getLogger(Block.class);
	public String s = "hello";

	public void testIf() {
		if (StringUtils.equals(s, "hello")) {
			logger.info(s);
		}
		if (!StringUtils.equals(s, "hello")) {
			logger.info(s);
		}
		if (StringUtils.isNotEmpty(s)) {
			logger.info(s);
		}
		if (!StringUtils.isNotEmpty(s)) {
			logger.info(s);
		} else {
			logger.info(s);
		}
		if (StringUtils.isNotEmpty(s)) {
			logger.info(s);
		}
		if (StringUtils.isNotEmpty(s)) {
			logger.info(s);
			logger.info("yes");
		}
	}

	public void testFor() {
		Map<String, Integer> map = Collection.newHashMap("key", 100, "key", 100);
		for (String key : map.keySet()) {
			logger.info(key);
		}
		List<String> list = Collection.newArrayList("first", "second", "third");
		for (String str : list) {
			logger.info(str);
		}
		for (int i = 0; i < 100; i++) {
			s = list.get(i);
			logger.info("thank {} very much!", "you");
			continue;
		}
		for (int i = 0; i < 100; i++) {
			logger.info("hello world!");
			s = String.valueOf(12345);
			s = "caixukun";
			break;
		}
		int[] nums = new int[100];
		for (int num : nums) {
			logger.info("num is {}", num);
		}
	}

	public void testWhile() {
		String y = "hi!";
		while (StringUtils.isNotEmpty(y)) {
			logger.info(y);
		}
	}

	public void testTry() {
		try {
			throw new Exception("test");
		} catch (Exception e) {
		}
	}

	public void testSync() {
		synchronized (s) {
			logger.info("in sync!");
		}
	}

}
