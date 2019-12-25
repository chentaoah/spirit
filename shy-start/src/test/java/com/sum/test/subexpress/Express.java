package com.sum.test.subexpress;

import com.sum.shy.lib.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sum.shy.lib.Collection;
import java.util.List;

public class Express {

	public static Logger logger = LoggerFactory.getLogger(Express.class);
	public int x = 1;
	public int y = 100;
	public String s = "test";

	public void testSubexpress() {
		if (((x + 1 > 0) && (y < 100)) && StringUtils.equals(s, "test")) {
			logger.info("hello");
		}
	}

	public void testCast() {
		String s1 = "I am a str";
		Object o1 = (Object)s1;
		logger.info("test success", o1);
	}

	public void testTree() {
		boolean b = (x + 1 > 0 && y < 100) && StringUtils.equals(s, "test") && s instanceof Object;
		logger.info("test tree", b);
		List<String> list = Collection.newArrayList("one", "two", "three");
		boolean b1 = ((Object)list.get(1)).toString().length() + 100 > 0;
		logger.info("tree", b1);
		String s1 = ((Object)list.get(1)).toString();
		logger.info("tree", s1);
		double d1 = 100.0;
		int i1 = 100;
		double num = d1 + i1;
		logger.info("tree", num);
		boolean express = (x + 1 > 0 && y < 100);
		logger.info("{}", express);
		Object express1 = ((Object)list.get(1));
		logger.info("{}", express1);
		boolean b11 = (x + 1 > 0 && y < 100) && StringUtils.equals(list.get(0), "test") && s instanceof Object;
		logger.info("{}", b11);
	}

}
