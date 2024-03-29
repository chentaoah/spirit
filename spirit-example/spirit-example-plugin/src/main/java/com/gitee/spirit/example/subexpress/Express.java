package com.gitee.spirit.example.subexpress;

import com.gitee.spirit.stdlib.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.gitee.spirit.stdlib.Emptys.empty;

public class Express {

	public static final Logger logger = LoggerFactory.getLogger(Express.class);
	public int x = 1;
	public int y = 100;
	public String s = "test";
	public double d = 1.1;
	public double e = d + 10;

	public void testSubexpress() {
		if (((x + 1 > 0) && (y < 100)) && StringUtils.equals(s, "test")) {
			logger.info("hello");
		}
	}

	public void testCast() {
		String s1 = "I am a str";
		Object o1 = (Object) s1;
		logger.info("test success", o1);
	}

	public String testTree() {
		boolean b = (x + 1 > 0 && y < 100) && StringUtils.equals(s, "test") && s instanceof Object;
		logger.info("test tree", b);
		List<String> list = Lists.of("one", "two", "three");
		boolean b1 = ((Object) list.get(1)).toString().length() + 100 > 0;
		logger.info("tree", b1);
		String s1 = ((Object) list.get(1)).toString();
		logger.info("tree", s1);
		double d1 = 100.0;
		int i1 = 100;
		double num = d1 + i1;
		logger.info("tree", num);
		boolean express = (x + 1 > 0 && y < 100);
		logger.info("{}", express);
		Object express1 = ((Object) list.get(1));
		logger.info("{}", express1);
		boolean b11 = (x + 1 > 0 && y < 100) && StringUtils.equals(list.get(0), "test") && s instanceof Object;
		logger.info("{}", b11);
		boolean b111 = (!StringUtils.equals(s, "test")) && StringUtils.equals(s, "test") && !empty(list.get(0));
		logger.info("{}", b111);
		String b222 = list.get(1).toString();
		logger.info("{}", b222);
		if (StringUtils.isNotEmpty(s)) {
			return null;
		} else {
			return "this is return type test";
		}
	}

}
