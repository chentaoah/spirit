package com.sum.spirit.core.compile;

import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import com.sum.spirit.common.utils.ConfigUtils;
import com.sum.spirit.common.utils.FileUrlUtils;
import com.sum.spirit.core.clazz.entity.IClass;

public abstract class AbstractCodeClassLoader extends AbstractClassLoader<IClass> implements InitializingBean {

	public List<URL> urls = new ArrayList<>();

	public Map<String, URL> nameUrlMapping = new LinkedHashMap<>();

	public Map<String, IClass> classes = new LinkedHashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		// 添加到urls
		String inputPath = ConfigUtils.getInputPath();
		String extension = ConfigUtils.getFileExtension();
		Collection<File> files = FileUtils.listFiles(new File(inputPath), new String[] { extension }, true);
		files.forEach(file -> this.urls.add(FileUrlUtils.toURL(file)));
		// 添加到映射
		File directory = new File(inputPath);
		if (!directory.isDirectory()) {
			throw new RuntimeException("The input path must be a directory!");
		}
		URL inputUrl = FileUrlUtils.toURL(directory);
		urls.forEach(url -> {
			String name = url.toString().replace(inputUrl.toString(), "").replaceAll("/", ".").replace("." + extension, "");
			nameUrlMapping.put(name, url);
			classes.put(name, null);
		});
	}

	@Override
	public List<URL> getResources() {
		return urls;
	}

	@Override
	public List<String> getNames() {
		return new ArrayList<String>(classes.keySet());
	}

	@Override
	public boolean contains(String name) {
		return classes.containsKey(name);
	}

	@Override
	public IClass findClass(String name) {
		IClass clazz = findLoadedClass(name);
		if (clazz == null && contains(name)) {
			clazz = loadClass(name);
		}
		return clazz;
	}

	@Override
	public IClass findLoadedClass(String name) {
		return classes.get(name);
	}

	@Override
	public List<IClass> getAllClasses() {
		return classes.values().stream().filter(clazz -> clazz != null).collect(Collectors.toList());
	}

	@Override
	public URL findResource(String name) {
		return nameUrlMapping.get(name);
	}

	@Override
	public String findClassName(String simpleName) {
		for (String className : classes.keySet()) {
			if (className.endsWith("." + simpleName)) {
				return className;
			}
		}
		return null;
	}

	public void clear() {
		classes.clear();
		nameUrlMapping.keySet().forEach(name -> classes.put(name, null));
	}

}
