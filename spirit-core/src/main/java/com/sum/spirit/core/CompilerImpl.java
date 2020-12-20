package com.sum.spirit.core;

import java.io.File;
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
	public DocumentReader reader;
	@Autowired
	public ClassResolver resolver;
	@Autowired
	public CodeClassLoader classLoader;
	@Autowired
	public AutoImporter importer;
	@Autowired
	public ClassVisiter visiter;

	@Override
	public List<IClass> compile(Map<String, File> files, String... includePaths) {
		// 提前将所有类名记录下来
		files.keySet().forEach(path -> classLoader.classes.put(path, null));
		// path -> (className -> class)
		Map<String, Map<String, IClass>> classesMap = new LinkedHashMap<>();
		// 解析指定类型
		files.forEach((path, file) -> {
			if (TypeUtils.matchPackages(path, includePaths)) {
				classesMap.put(path, doCompile(files, path));
			}
		});
		// 分析依赖项
		classesMap.values().forEach(classes -> {
			classes.forEach((className, clazz) -> {
				Set<String> dependencies = importer.dependencies(clazz);
				dependencies.forEach(dependency -> {
					if (classLoader.contains(dependency) && !classLoader.isloaded(dependency)) {
						doCompile(files, dependency);// 注意：这里间接要求，部分编译时，依赖项目不能是内部类
					}
				});
				importer.autoImport(clazz, dependencies);
			});
		});
		// 进行推导
		List<IClass> classes = classLoader.getClasses();
		classes.forEach(clazz -> visiter.prevVisitClass(clazz));
		classes.forEach(clazz -> visiter.visitClass(clazz));
		return classes;
	}

	public Map<String, IClass> doCompile(Map<String, File> files, String path) {
		Document document = reader.readFile(files.get(path));
		Map<String, IClass> classes = resolver.resolve(TypeUtils.getPackage(path), document);
		classLoader.classes.putAll(classes);
		return classes;
	}

}
