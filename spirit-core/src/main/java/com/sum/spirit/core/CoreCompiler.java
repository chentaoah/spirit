package com.sum.spirit.core;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.element.impl.Document;
import com.sum.spirit.utils.TypeUtils;

import cn.hutool.core.util.ArrayUtil;

@Component
@Primary
public class CoreCompiler implements Compiler {

	@Autowired
	public CodeClassLoader classLoader;
	@Autowired
	public DocumentReader reader;
	@Autowired
	public ClassResolver resolver;
	@Autowired
	public AutoImporter importer;
	@Autowired
	public ClassVisiter visiter;

	@Override
	public List<IClass> compile(Map<String, InputStream> inputs, String... includePaths) {
		List<IClass> classes = loadClasses(inputs, includePaths);
		return visitClasses(classes);
	}

	public List<IClass> loadClasses(Map<String, InputStream> inputs, String... includePaths) {
		// 因为这个方法可能会反复调用
		classLoader.classes.clear();
		// 提前将所有类名记录下来
		inputs.keySet().forEach(path -> classLoader.classes.put(path, null));
		// path -> (className -> class)
		Map<String, Map<String, IClass>> classesMap = new LinkedHashMap<>();
		// 如果没有排除，则进行全量编译
		if (ArrayUtil.isEmpty(includePaths)) {
			loadAllClasses(inputs, classesMap);
		} else {
			loadPartialClasses(inputs, classesMap, includePaths);
		}
		// 进行推导
		return classLoader.getClasses();
	}

	public List<IClass> visitClasses(List<IClass> classes) {
		classes.forEach(clazz -> importer.autoImport(clazz));
		classes.forEach(clazz -> visiter.prepareForVisit(clazz));
		classes.forEach(clazz -> visiter.visitClass(clazz));
		return classes;
	}

	public void loadAllClasses(Map<String, InputStream> inputs, Map<String, Map<String, IClass>> classesMap) {
		inputs.forEach((path, file) -> classesMap.put(path, doCompile(inputs, path)));
	}

	public void loadPartialClasses(Map<String, InputStream> inputs, Map<String, Map<String, IClass>> classesMap, String... includePaths) {
		// 解析指定类型
		inputs.forEach((path, file) -> {
			if (TypeUtils.matchPackages(path, includePaths)) {
				classesMap.put(path, doCompile(inputs, path));
			}
		});
		// 分析依赖项
		classesMap.values().forEach(classes -> {
			dependencies(inputs, classes);
		});
	}

	public Map<String, IClass> doCompile(Map<String, InputStream> inputs, String path) {
		Document document = reader.readDocument(TypeUtils.getLastName(path), inputs.get(path));
		Map<String, IClass> classes = resolver.resolveClasses(TypeUtils.getPackage(path), document);
		classLoader.classes.putAll(classes);
		return classes;
	}

	public void dependencies(Map<String, InputStream> inputs, Map<String, IClass> classes) {
		classes.forEach((className, clazz) -> {
			Set<String> dependencies = importer.dependencies(clazz);
			dependencies.forEach(dependency -> {
				if (classLoader.contains(dependency) && !classLoader.isloaded(dependency)) {
					// 注意：这里间接要求，部分编译时，依赖项目不能是内部类
					dependencies(inputs, doCompile(inputs, dependency));
				}
			});
		});
	}

}
