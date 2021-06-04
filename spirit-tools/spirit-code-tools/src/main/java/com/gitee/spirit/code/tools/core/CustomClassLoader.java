package com.gitee.spirit.code.tools.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.utils.URLFileUtils;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.compile.AppClassLoader;

@Primary
@Component
@DependsOn("configUtils")
public class CustomClassLoader extends AppClassLoader {

	public IClass loadClass(String name, InputStream input, String... arguments) {
		// 清理所有的上一次编译的缓存信息
		clear();
		Map<String, IClass> classes = compiler.compile(name, input, arguments);
		this.classes.putAll(classes);
		resolveClasses(classes);
		visitClasses(getAllClasses());
		return loadClass(name);
	}

	public void resolveClasses(Map<String, IClass> classes) {
		classes.values().forEach(clazz -> {
			Set<String> classNames = importer.dependencies(clazz);
			classNames.forEach(className -> {
				if (contains(className) && loadClass(className) == null) {
					// 注意：这里间接要求，部分编译时，依赖项目不能是内部类
					Map<String, IClass> classes0 = compiler.compile(className, URLFileUtils.asStream(getResource(className)));
					this.classes.putAll(classes0);
					resolveClasses(classes0);
				}
			});
		});
	}

	public String getName(String filePath) {
		URL fileUrl = URLFileUtils.toURL(new File(filePath));
		for (Map.Entry<String, URL> entry : urls.entrySet()) {
			if (fileUrl.sameFile(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

}
