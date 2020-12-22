package com.sum.spirit.core;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.element.impl.Document;
import com.sum.spirit.utils.TypeUtils;

@Component
public class CompilerImpl implements Compiler {

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
	public List<IClass> compile(Map<String, ? extends InputStream> inputs, String... includePaths) {
		// 提前将所有类名记录下来
		inputs.keySet().forEach(path -> classLoader.classes.put(path, null));
		// path -> (className -> class)
		Map<String, Map<String, IClass>> classesMap = new LinkedHashMap<>();
		// 解析指定类型
		inputs.forEach((path, file) -> {
			if (TypeUtils.matchPackages(path, includePaths)) {
				classesMap.put(path, doCompile(inputs, path));
			}
		});
		// 分析依赖项
		classesMap.values().forEach(classes -> {
			compileDependencies(inputs, classes);
		});
		// 进行推导
		List<IClass> classes = classLoader.getClasses();
		classes.forEach(clazz -> importer.autoImport(clazz));
		classes.forEach(clazz -> visiter.prepareForVisit(clazz));
		classes.forEach(clazz -> visiter.visitClass(clazz));
		return classes;
	}

	public Map<String, IClass> doCompile(Map<String, ? extends InputStream> inputs, String path) {
		Document document = reader.readLines(TypeUtils.getLastName(path), inputs.get(path));
		Map<String, IClass> classes = resolver.resolve(TypeUtils.getPackage(path), document);
		classLoader.classes.putAll(classes);
		return classes;
	}

	public void compileDependencies(Map<String, ? extends InputStream> inputs, Map<String, IClass> classes) {
		classes.forEach((className, clazz) -> {
			Set<String> dependencies = importer.dependencies(clazz);
			dependencies.forEach(dependency -> {
				if (classLoader.contains(dependency) && !classLoader.isloaded(dependency)) {
					// 注意：这里间接要求，部分编译时，依赖项目不能是内部类
					compileDependencies(inputs, doCompile(inputs, dependency));
				}
			});
		});
	}

}
