package com.gitee.spirit.example.syntax;

import com.gitee.spirit.stdlib.Lists;
import com.gitee.spirit.stdlib.Maps;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Syntax {

	public static final Logger logger = LoggerFactory.getLogger(Syntax.class);
	public List<String> list = Lists.of("first", "second");
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
