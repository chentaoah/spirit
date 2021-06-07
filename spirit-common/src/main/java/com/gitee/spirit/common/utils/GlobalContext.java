package com.gitee.spirit.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class GlobalContext {

	@Autowired
	public ApplicationContext context;

	public <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}

	public <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}

	public <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public <T> List<T> getBeans(Class<T> requiredType) {
		Map<String, T> beanMap = getBeansOfType(requiredType);
		List<T> beans = new ArrayList<>(beanMap.values());
		beans.sort(new AnnotationAwareOrderComparator());
		return beans;
	}

}
