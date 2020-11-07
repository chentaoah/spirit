package com.sum.spirit.plug.link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.AbsClassLoader;
import com.sum.spirit.plug.annotation.Data;

@Component
@Order(-60)
public class ExtentionClassLoader extends AbsClassLoader {

	public static final Map<String, String> TYPE_MAPPING = new ConcurrentHashMap<>();

	static {
		TYPE_MAPPING.put(Data.class.getSimpleName(), Data.class.getName());
	}

	@Override
	public String getClassName(String simpleName) {
		return TYPE_MAPPING.get(simpleName);
	}

	@Override
	public boolean isLoaded(String className) {
		return Data.class.getName().equals(className);
	}

}
