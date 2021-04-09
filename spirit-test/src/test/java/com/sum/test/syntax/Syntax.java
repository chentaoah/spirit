package com.sum.test.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import java.util.List;
import com.sum.spirit.lib.Maps;
import java.util.Map;

public class Syntax {

	public static final Logger logger = LoggerFactory.getLogger(Syntax.class);
	public List<String> list = Lists.newArrayList("first", "second");
	public Map<String, Integer> map = Maps.of("key", 100, "key", 100);

	public void testJudge() {
		boolean b = list.get(1) != null;
		boolean bb = list instanceof Object;
		if (b && bb) {
			logger.info("yes");
		}
		if (list.get(1) != null && list instanceof Object) {
			logger.info("test success");
		}
	}

	public void testLog() {
		try {
			logger.info("test print keyword");
			logger.debug("test debug keyword");
			throw new Exception("test");
		} catch (Exception e) {
			logger.error("There is a Exception!", e);
		}
	}

}
