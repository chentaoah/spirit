package com.gitee.spirit.output.java;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
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

    public ClassLoader loader;

    @Override
    public void afterPropertiesSet() {
        String classPathsStr = ConfigUtils.getClassPaths();
        if (StringUtils.isNotBlank(classPathsStr)) {
            List<String> classPaths = Splitter.on(",").trimResults().splitToList(classPathsStr);
            loader = ReflectUtils.getClassLoader(classPaths);
        } else {
            loader = this.getClass().getClassLoader();
        }
    }

    @Override
    public URL getResource(String name) {
        return loader.getResource(name);
    }

    @Override
    public List<String> getNames() {
        throw new RuntimeException("This method is not supported!");
    }

    @Override
    public boolean contains(String name) {
        try {
            return loadClass(name) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Class<?> loadClass(String name) {
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Class<?>> getAllClasses() {
        throw new RuntimeException("This method is not supported!");
    }

    @Override
    public Class<?> defineClass(String name, URL resource) {
        throw new RuntimeException("This method is not supported!");
    }

}
