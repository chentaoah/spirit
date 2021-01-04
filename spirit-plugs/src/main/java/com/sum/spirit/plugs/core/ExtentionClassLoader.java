package com.sum.spirit.plugs.core;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.AbstractClassLoader;

@Component
@Order(-60)
public class ExtentionClassLoader extends AbstractClassLoader {

	@Override
	public String findClassName(String simpleName) {
		return null;
	}

	@Override
	public boolean contains(String className) {
		return false;
	}

	@Override
	public <T> T getClass(String className) {
		return null;
	}

}
