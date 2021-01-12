package com.sum.spirit.core;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.utils.FileHelper;

@Component
@Order(-100)
public class AppClassLoader extends AbstractCodeClassLoader {

	@Autowired
	public Compiler compiler;
	@Autowired
	public AutoImporter importer;
	@Autowired
	public ClassVisiter visiter;

	@Override
	public List<IClass> getAllClasses() {
		if (classes.isEmpty()) {
			List<String> names = getNames();
			names.forEach(name -> findClass(name));
		}
		List<IClass> classes = super.getAllClasses();
		visitClasses(classes);
		return classes;
	}

	@Override
	public IClass defineClass(String name, URL resource) {
		Map<String, IClass> classes = compiler.compile(name, FileHelper.asStream(resource));
		this.classes.putAll(classes);// 提前将内部类添加到上下文中

		Map<String, IClass> allClasses = new LinkedHashMap<>();
		resolveClasses(allClasses, classes);
		this.classes.putAll(allClasses);// 添加到上下文中

		return classes.get(name);
	}

	public void resolveClasses(Map<String, IClass> allClasses, Map<String, IClass> classes) {
		classes.values().forEach(clazz -> {
			// 分析依赖项
			Set<String> classNames = importer.dependencies(clazz);
			classNames.forEach(className -> {
				// 添加依赖项
				clazz.addImport(className);
				// 依赖项存在于集合中，并且没有被加载，并且不在本次编译对象中
				if (contains(className) && findLoadedClass(className) == null && !allClasses.containsKey(className)) {
					// 注意：这里间接要求，部分编译时，依赖项目不能是内部类
					Map<String, IClass> classes0 = compiler.compile(className, FileHelper.asStream(findResource(className)));
					allClasses.putAll(classes0);
					resolveClasses(allClasses, classes0);
				}
			});
		});
	}

	public void visitClasses(List<IClass> classes) {
		classes.forEach(clazz -> visiter.prepareForVisit(clazz));
		classes.forEach(clazz -> visiter.visitClass(clazz));
	}

}
