package com.sum.test.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sum.shy.library.Collection;
import java.util.List;
import java.util.Map;

public class Syntax {

	public static Logger logger = LoggerFactory.getLogger(Syntax.class);
	public List<String> list = Collection.newArrayList("first", "second");
	public Map<String, Integer> map = Collection.newHashMap("key", 100, "key", 100);

	public void testFastAdd() {
		list.add("first");
		list.add("second");
		map.put("user1", 123);
		map.put("user2", 456);
	}

	public void testJudge() {
		boolean b = list.get(1) != null;
		boolean bb = list instanceof Object;
		if (b && bb) {
			logger.info("yes");
		}
	}

	public void testJudgeInvoke() {
		if (list != null)
			list.get(100);
	}

	public void testError() {
		try {
			logger.info("test print keyword");
			logger.debug("test debug keyword");
			throw new Exception("test");
		} catch (Exception e) {
			logger.error("There is a Exception!", e);
		}
	}

}
