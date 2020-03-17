package com.sum.test.auto;

import com.sum.test.auto.Friend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Import {

	public static Logger logger = LoggerFactory.getLogger(Import.class);

	public Friend f = new Friend();

	public com.sum.test.auto.Alias a = new com.sum.test.auto.Alias();

	public String s = "Clock moved backwards.com.sum.test.auto.Alias to generate id for %d milliseconds";

	public String testFriend() {
		return f.sayHello();
	}

	public String testAlias() {
		return a.getName();
	}

	public void testStr() {
		logger.info( s );
	}

}
