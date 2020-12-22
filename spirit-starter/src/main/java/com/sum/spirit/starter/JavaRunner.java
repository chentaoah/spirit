package com.sum.spirit.starter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.ConfigUtils;

@Component
public class JavaRunner implements ApplicationRunner {

	@Autowired
	public JavaEngine engine;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 是否自动编译
		boolean autoRun = ConfigUtils.getProperty(Constants.AUTO_RUN_KEY, false);
		if (!autoRun) {
			return;
		}
		String inputPath = args.getOptionValues(Constants.INPUT_ARG_KEY).get(0);
		String outputPath = args.containsOption(Constants.OUTPUT_ARG_KEY) ? args.getOptionValues(Constants.OUTPUT_ARG_KEY).get(0) : null;
		String extension = ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY, Constants.DEFAULT_FILENAME_EXTENSION);
		Map<String, Object> params = new HashMap<>();
		params.put(Constants.INPUT_ARG_KEY, inputPath);
		params.put(Constants.OUTPUT_ARG_KEY, outputPath);
		params.put(Constants.FILENAME_EXTENSION_KEY, extension);
		engine.run(params);
	}

}
