package com.sum.spirit.plugs.core;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.AbsClassLoader;

@Component
@Order(-60)
public class ExtentionClassLoader extends AbsClassLoader {

	@Override
	public String getClassName(String simpleName) {
		return null;
	}

	@Override
	public boolean isLoaded(String className) {
		return false;
	}

	@Override
	public <T> T getClass(String className) {
		return null;
	}

}
