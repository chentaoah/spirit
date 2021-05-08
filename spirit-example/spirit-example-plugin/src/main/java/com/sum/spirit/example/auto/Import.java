package com.sum.spirit.example.auto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Import {

	public static final Logger logger = LoggerFactory.getLogger(Import.class);
	public Friend f = new Friend();
	public com.sum.spirit.example.auto.Alias a = new com.sum.spirit.example.auto.Alias();
	public String xxxxG_Alias = "Clock moved backwards.G_Alias to generate id for %d milliseconds";

	public String testFriend(String text, Integer number) {
		return f.sayHello2();
	}

	public String testAlias() {
		return a.getName();
	}

	public void testStr() {
		logger.info(xxxxG_Alias);
	}

}
