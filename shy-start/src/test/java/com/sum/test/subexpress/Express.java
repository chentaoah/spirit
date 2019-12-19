package com.sum.test.subexpress;

import com.sum.shy.lib.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
