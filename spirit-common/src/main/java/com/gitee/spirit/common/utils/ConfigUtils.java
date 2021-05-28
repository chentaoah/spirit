package com.gitee.spirit.common.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.AppConfig.Argument;
import com.gitee.spirit.common.constants.AppConfig.DefaultValue;

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
		return ConfigUtils.getProperty(Argument.INPUT);
	}

	public static String getOutputPath() {
		return ConfigUtils.getProperty(Argument.OUTPUT);
	}

	public static String getClasspaths() {
		return ConfigUtils.getProperty(Argument.CLASSPATHS);
	}

	public static String getLangPackage() {
		return ConfigUtils.getProperty(Argument.LANG_PACKAGE);
	}

	public static String getUtilPackage() {
		return ConfigUtils.getProperty(Argument.UTIL_PACKAGE);
	}

	public static String getFileExtension() {
		return ConfigUtils.getProperty(Argument.FILE_EXTENSION, DefaultValue.FILE_EXTENSION);
	}

	public static boolean isDebug() {
		return ConfigUtils.getProperty(Argument.DEBUG, DefaultValue.DEBUG);
	}

}
