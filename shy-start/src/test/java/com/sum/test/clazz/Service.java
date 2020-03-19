package com.sum.test.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Service {

	public static Logger logger = LoggerFactory.getLogger(Service.class);

	public String one();

	public int two();

	public byte[] three();

	public static void main(String[] args) {
		logger.info("haha");
	}

	public static void doSomething(String text) {
		logger.info("hello");
	}

}
