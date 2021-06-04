package com.gitee.spirit.output.java;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.utils.ConfigUtils;
import com.gitee.spirit.core.clazz.AbstractClassLoader;
import com.gitee.spirit.output.java.utils.ReflectUtils;
import com.google.common.base.Splitter;

@Component
@Order(-80)
@DependsOn("configUtils")
public class ExtClassLoader extends AbstractClassLoader<Class<?>> implements InitializingBean {

	public ClassLoader classLoader;

	@Override
	public void afterPropertiesSet() throws Exception {
		String classpathsStr = ConfigUtils.getClasspaths();
		if (StringUtils.isNotBlank(classpathsStr)) {
			List<String> classpaths = Splitter.on(",").trimResults().splitToList(classpathsStr);
			classLoader = ReflectUtils.getClassLoader(classpaths);
		} else {
			classLoader = this.getClass().getClassLoader();
		}
	}

	@Override
	public List<URL> getResources() {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public List<String> getNames() {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public boolean contains(String name) {
		return name.startsWith("java.lang.");
	}

	@Override
	public Class<?> findClass(String name) {
		try {
			return classLoader.loadClass(name);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<?> findLoadedClass(String name) {
		return findClass(name);
	}

	@Override
	public List<Class<?>> getAllClasses() {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public URL findResource(String name) {
		throw new RuntimeException("This method is not supported!");
	}

	@Override
	public Class<?> defineClass(String name, URL resource) {
		throw new RuntimeException("This method is not supported!");
	}

}
