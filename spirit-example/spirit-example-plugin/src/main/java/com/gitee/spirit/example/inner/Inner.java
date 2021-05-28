package com.gitee.spirit.example.inner;

import com.gitee.spirit.example.deduce.Father;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inner {

	public static final Logger logger = LoggerFactory.getLogger(Inner.class);
	public String f1 = "I am Inner!";
	public String f2 = "hello world!";

	public int getAge() {
		return 18;
	}

	public void testImport() {
		logger.info(new Father().toString());
	}

}
