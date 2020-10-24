package com.sum.spirit.plug.link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.sum.spirit.api.ExtendedLoader;
import com.sum.spirit.plug.annotation.Data;

@Component
public class ExtendedLoaderImpl implements ExtendedLoader {

	public static final Map<String, String> TYPE_MAPPING = new ConcurrentHashMap<>();

	static {
		TYPE_MAPPING.put(Data.class.getSimpleName(), Data.class.getName());
	}

	@Override
	public String findExtendedType(String simpleName) {
		return TYPE_MAPPING.get(simpleName);
	}

}
