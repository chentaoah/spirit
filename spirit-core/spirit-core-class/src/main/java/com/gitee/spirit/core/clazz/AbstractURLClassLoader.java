package com.gitee.spirit.core.clazz;

import java.io.File;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import com.gitee.spirit.common.utils.ConfigUtils;
import com.gitee.spirit.common.utils.URLFileUtils;
import com.gitee.spirit.core.clazz.entity.IClass;

import cn.hutool.core.lang.Assert;

public abstract class AbstractURLClassLoader extends AbstractClassLoader<IClass> implements InitializingBean {

    public Map<String, URL> urls = new LinkedHashMap<>();
    public Map<String, IClass> classes = new LinkedHashMap<>();

    @Override
    public void afterPropertiesSet() {
        String inputPath = ConfigUtils.getInputPath();
        String extension = ConfigUtils.getFileExtension();
        Collection<File> files = FileUtils.listFiles(new File(inputPath), new String[]{extension}, true);
        List<URL> urlList = new ArrayList<>();
        files.forEach(file -> urlList.add(URLFileUtils.toURL(file)));
        File directory = new File(inputPath);
        Assert.isTrue(directory.isDirectory(), "The input path must be a directory!");
        URL inputUrl = URLFileUtils.toURL(directory);
        urlList.forEach(url -> {
            String name = url.toString().replace(inputUrl.toString(), "").replaceAll("/", ".");
            name = name.endsWith("." + extension) ? name.substring(0, name.lastIndexOf('.')) : name;
            urls.put(name, url);
            classes.put(name, null);
        });
    }

    @Override
    public URL getResource(String name) {
        return urls.get(name);
    }

    @Override
    public List<String> getNames() {
        return new ArrayList<>(classes.keySet());
    }

    @Override
    public boolean contains(String name) {
        return classes.containsKey(name);
    }

    @Override
    public IClass loadClass(String name) {
        Assert.isTrue(contains(name), "The class was not found!name:" + name);
        return classes.computeIfAbsent(name, super::loadClass);
    }

    @Override
    public List<IClass> getAllClasses() {
        return classes.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void clear() {
        classes.clear();
        urls.keySet().forEach(name -> classes.put(name, null));
    }

}
