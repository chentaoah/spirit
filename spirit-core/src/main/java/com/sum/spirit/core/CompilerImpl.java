package com.sum.spirit.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	public ClassVisiter visiter;
	@Autowired
	public CodeClassLoader classLoader;
	@Autowired
	public PostProcessor processor;

	@Override
	public Map<String, IClass> compile(Map<String, File> files) {
		Map<String, IClass> allClasses = new LinkedHashMap<>();
		files.forEach((path, file) -> {
			// 1.读取文件
			Document document = reader.readFile(file);
			processor.whenDocumentReadFinish(path, document);
			// 2.解析类型
			List<IClass> classes = resolver.resolve(TypeUtils.getPackage(path), document);
			classes.forEach(clazz -> allClasses.put(clazz.getClassName(), clazz));
		});
		// 3.放入上下文
		classLoader.classes = allClasses;
		processor.whenClassesLoadFinish(allClasses);
		// 4.进行类型成员变量的推导
		visiter.visitClasses(allClasses);
		return allClasses;
	}

}
