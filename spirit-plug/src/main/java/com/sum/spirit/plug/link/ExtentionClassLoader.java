package com.sum.spirit.plug.link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.plug.annotation.Data;

@Component
@Order(-60)
public class ExtentionClassLoader implements ClassLoader {

	public static final Map<String, String> TYPE_MAPPING = new ConcurrentHashMap<>();

	static {
		TYPE_MAPPING.put(Data.class.getSimpleName(), Data.class.getName());
	}

	@Override
	public void load() {
		// ignore
	}

	@Override
	public String getClassName(String simpleName) {
		return TYPE_MAPPING.get(simpleName);
	}

	@Override
	public boolean isLoaded(String className) {
		return Data.class.getName().equals(className);
	}

	@Override
	public boolean shouldImport(String className) {
		return true;
	}

	@Override
	public <T> T getClass(String className) {
		return null;
	}

}
