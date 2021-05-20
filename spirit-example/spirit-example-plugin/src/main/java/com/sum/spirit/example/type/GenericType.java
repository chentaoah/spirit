package com.sum.spirit.example.type;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class GenericType<T, K> extends HashMap<T, Integer> {

	public static final Logger logger = LoggerFactory.getLogger(GenericType.class);

	public Integer testGeneric() {
		return get("test");
	}

	public HashMap<T, K> test() {
		return new HashMap<T, K>();
	}

	public Integer test1() {
		GenericType<String, String> g = new GenericType<String, String>();
		return g.get("test");
	}

	public HashMap<String, String> test2() {
		GenericType<String, String> g = new GenericType<String, String>();
		return g.test();
	}

	public void testB() {
		Class<Integer> clazz = int.class;
		logger.info(clazz.getName());
	}

}
