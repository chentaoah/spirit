package com.gitee.spirit.core.clazz;

import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import com.gitee.spirit.common.utils.ConfigUtils;
import com.gitee.spirit.common.utils.URLFileUtils;
import com.gitee.spirit.core.clazz.entity.IClass;

import cn.hutool.core.lang.Assert;

public abstract class AbstractURLClassLoader extends AbstractClassLoader<IClass> implements InitializingBean {

	public List<URL> urls = new ArrayList<>();
	public Map<String, URL> nameUrlMapping = new LinkedHashMap<>();
	public Map<String, IClass> classes = new LinkedHashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		String inputPath = ConfigUtils.getInputPath();
		String extension = ConfigUtils.getFileExtension();
		Collection<File> files = FileUtils.listFiles(new File(inputPath), new String[] { extension }, true);
		files.forEach(file -> this.urls.add(URLFileUtils.toURL(file)));
		File directory = new File(inputPath);
		Assert.isTrue(directory.isDirectory(), "The input path must be a directory!");
		URL inputUrl = URLFileUtils.toURL(directory);
		urls.forEach(url -> {
			String name = url.toString().replace(inputUrl.toString(), "").replaceAll("/", ".");
			if (name.endsWith("." + extension)) {
				name = name.substring(0, name.lastIndexOf('.'));
			}
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
		return classes.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public URL findResource(String name) {
		return nameUrlMapping.get(name);
	}

	public void clear() {
		classes.clear();
		nameUrlMapping.keySet().forEach(name -> classes.put(name, null));
	}

}
