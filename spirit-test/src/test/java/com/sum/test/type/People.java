package com.sum.test.type;

import com.sum.spirit.plug.annotation.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class People {

	public static final Logger logger = LoggerFactory.getLogger(People.class);
	public String name = "chentao";
	public int age = 28;
	public double money = -10.0;

	public Object show() {
		logger.info(name);
		logger.info("age:{}", age);
		logger.info("money:{}", money);
		return null;
	}

}
