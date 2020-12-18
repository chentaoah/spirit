package com.sum.spirit.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtils implements EnvironmentAware {

	public static Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		ConfigUtils.environment = environment;
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

}
