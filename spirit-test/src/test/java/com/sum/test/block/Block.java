package com.sum.test.block;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sum.spirit.utils.Maps;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.Lists;
import java.util.List;

public class Block {

	public static final Logger logger = LoggerFactory.getLogger(Block.class);
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
		Map<String, Integer> map = Maps.of("key", 100, "key", 100);
		for (String key : map.keySet()) {
			logger.info(key);
		}
		for (Integer value : map.values()) {
			logger.info("number is {}", value);
		}
		for (Entry<String, Integer> entry : map.entrySet()) {
			logger.info("test entry!", entry.getKey());
		}
		List<String> list = Lists.newArrayList("first", "second", "third");
		for (String str : list) {
			logger.info(str);
			break;
		}
		for (int i = 0; i < list.size(); i++) {
			s = list.get(i);
			logger.info("thank {} very much!", "you");
			continue;
		}
		for (int i = 0; i < list.size(); i++) {
			s = list.get(i);
			s = String.valueOf(12345);
			s = "caixukun";
			continue;
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
			break;
		}
	}

	public String testTry() {
		try {
			if (StringUtils.equals(s, "hello")) {
				throw new Exception("test");
			}
			return s;
		} catch (Exception e) {
			logger.error("error is", e);
		} finally {
			logger.info("hello");
		}
		return null;
	}

	public synchronized void testSync() {
		synchronized (s) {
			logger.info("in sync!");
		}
	}

	public String testThrows() throws Exception {
		return s;
	}

	public void testGetLines() {
		if (StringUtils.equals(s, "hello\\")) {
			logger.info("test");
		}
		if (StringUtils.equals(s, "hello")) {
			logger.info("test}");
		}
	}

}
