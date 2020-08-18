package com.sum.test.process;

import com.sum.soon.lib.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static Logger logger = LoggerFactory.getLogger(Main.class);
	public static int x = 100;
	public static double y = 100.0;

	public static void main(String[] args) {
		doSomething();
	}

	public static boolean doSomething() {
		String s = "hello";
		if (StringUtils.equals(s, "hello")) {
			logger.info("hi!");
			return true;
		}
		return false;
	}

}
