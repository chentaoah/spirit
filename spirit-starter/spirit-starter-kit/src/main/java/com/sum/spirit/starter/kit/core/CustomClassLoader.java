package com.sum.spirit.starter.kit.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.FileURLUtils;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.compile.AppClassLoader;

@Component
@Primary
@DependsOn("configUtils")
public class CustomClassLoader extends AppClassLoader {

	public IClass loadClass(String name, InputStream input, String... arguments) {
		// 清理所有的上一次编译的缓存信息
		clear();
		Map<String, IClass> classes = compiler.compile(name, input, arguments);
		this.classes.putAll(classes);
		resolveClasses(classes);
		visitClasses(getAllClasses());
		return findClass(name);
	}

	public void resolveClasses(Map<String, IClass> classes) {
		classes.values().forEach(clazz -> {
			Set<String> classNames = importer.dependencies(clazz);
			classNames.forEach(className -> {
				if (contains(className) && findLoadedClass(className) == null) {
					// 注意：这里间接要求，部分编译时，依赖项目不能是内部类
					Map<String, IClass> classes0 = compiler.compile(className, FileURLUtils.asStream(findResource(className)));
					this.classes.putAll(classes0);
					resolveClasses(classes0);
				}
			});
		});
	}

	public String getName(String filePath) {
		URL fileUrl = FileURLUtils.toURL(new File(filePath));
		for (Map.Entry<String, URL> entry : nameUrlMapping.entrySet()) {
			if (fileUrl.sameFile(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

}
