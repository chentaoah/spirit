package com.sum.shy.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		int a = 100;
		int b = 100;
		Map<String, Boolean> map = new HashMap<>();
		map.put("key", a == b);
		map.put("key", 10 > 100);
		synchronized (map) {

		}
		List<String> list = new ArrayList<String>();
		list.add("word");
		list.add("word");
		list.add("word");
		list.add("word");
		list.add("word");
		list.add("word");

		if (list instanceof Object) {

		}

	}

}
