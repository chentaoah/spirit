package com.sum.test.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sum.shy.library.StringUtils;

public class Main {

	public static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("hello world!");
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
