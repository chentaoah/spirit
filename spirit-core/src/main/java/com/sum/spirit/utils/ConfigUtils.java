package com.sum.spirit.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.common.Constants;

@Component
public class ConfigUtils implements EnvironmentAware {

	public static Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		ConfigUtils.environment = environment;
	}

	public static boolean contains(String key) {
		return environment.containsProperty(key);
	}

	public static String getProperty(String key) {
		return environment.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return environment.getProperty(key, defaultValue);
	}

	public static Boolean getProperty(String key, Boolean defaultValue) {
		return environment.containsProperty(key) ? environment.getProperty(key, Boolean.class) : defaultValue;
	}

	public static Integer getProperty(String key, Integer defaultValue) {
		return environment.containsProperty(key) ? environment.getProperty(key, Integer.class) : defaultValue;
	}

	public static Long getProperty(String key, Long defaultValue) {
		return environment.containsProperty(key) ? environment.getProperty(key, Long.class) : defaultValue;
	}

	public static String getInputPath() {
		return ConfigUtils.getProperty(Constants.INPUT_ARG_KEY);
	}

	public static String getOutputPath() {
		return ConfigUtils.getProperty(Constants.OUTPUT_ARG_KEY);
	}

	public static String getFileExtension() {
		return ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY, Constants.DEFAULT_FILENAME_EXTENSION);
	}

	public static boolean isAutoRun() {
		return ConfigUtils.getProperty(Constants.AUTO_RUN_KEY, Constants.DEFAULT_AUTO_RUN);
	}

	public static boolean isDocumentDebug() {
		return ConfigUtils.getProperty(Constants.DOCUMENT_DEBUG_KEY, Constants.DEFAULT_DOCUMENT_DEBUG);
	}

	public static boolean isSyntaxCheck() {
		return ConfigUtils.getProperty(Constants.SYNTAX_CHECK_KEY, Constants.DEFAULT_SYNTAX_CHECK);
	}

}
