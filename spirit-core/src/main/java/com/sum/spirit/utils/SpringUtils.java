package com.sum.spirit.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

	public static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static <T> List<T> getBeansAndSort(Class<T> type) {
		Map<String, T> beanMap = context.getBeansOfType(type);
		List<T> beans = new ArrayList<>(beanMap.values());
		beans.sort(new AnnotationAwareOrderComparator());
		return beans;
	}

	public static <T> List<T> getBeansAndSort(Class<T> type, Class<?> excludedType) {
		List<T> beans = getBeansAndSort(type);
		return beans.stream().filter((t) -> t.getClass() != excludedType).collect(Collectors.toList());
	}

	public static <T> List<T> getBeansAndSort(Class<T> type, String scanPackage) {
		List<T> beans = getBeansAndSort(type);
		return beans.stream().filter((t) -> t.getClass().getName().startsWith(scanPackage)).collect(Collectors.toList());
	}
}