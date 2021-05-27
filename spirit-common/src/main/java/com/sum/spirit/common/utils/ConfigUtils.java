package com.sum.spirit.common.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.constants.AppConfig;

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
		return ConfigUtils.getProperty(AppConfig.INPUT_ARG_KEY);
	}

	public static String getOutputPath() {
		return ConfigUtils.getProperty(AppConfig.OUTPUT_ARG_KEY);
	}

	public static String getClasspaths() {
		return ConfigUtils.getProperty(AppConfig.CLASSPATHS_ARG_KEY);
	}

	public static String getLangPackage() {
		return ConfigUtils.getProperty(AppConfig.LANG_PKG_ARG_KEY);
	}

	public static String getUtilPackage() {
		return ConfigUtils.getProperty(AppConfig.UTIL_PKG_ARG_KEY);
	}

	public static String getFileExtension() {
		return ConfigUtils.getProperty(AppConfig.FILENAME_EXTENSION_KEY, AppConfig.DEFAULT_FILENAME_EXTENSION);
	}

	public static boolean isDebug() {
		return ConfigUtils.getProperty(AppConfig.DEBUG_KEY, AppConfig.DEFAULT_DEBUG);
	}

}
