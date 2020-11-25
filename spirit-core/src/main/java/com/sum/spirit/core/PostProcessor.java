package com.sum.spirit.core;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassEnhancer;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Document;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class PostProcessor implements InitializingBean {

	@Autowired
	public AutoImporter importer;
	@Autowired
	public AliasReplacer replacer;

	public long timestamp;

	public List<ClassEnhancer> enhancers;

	@Override
	public void afterPropertiesSet() throws Exception {
		enhancers = SpringUtils.getBeansAndSort(ClassEnhancer.class);
	}

	public void whenApplicationStart(String[] args) {
		for (String arg : args) {
			System.out.println(arg);
		}
		System.out.println("");
		timestamp = System.currentTimeMillis();
	}

	public void whenDocumentReadFinish(String path, Document document) {
		document.debug();
	}

	public void whenClassesLoadFinish(Map<String, IClass> allClasses) {
		allClasses.forEach((className, clazz) -> importer.visitClass(clazz));
	}

	public void whenClassCompileFinish(IClass clazz) {
		enhancers.forEach((enhancer) -> enhancer.enhance(clazz));
	}

	public String processCode(IClass clazz, String code) {
		code = replacer.replace(clazz, code);
		System.out.println(code);
		return code;
	}

	public void whenApplicationEnd(String[] args, Map<String, File> files) {
		System.out.println("Total compilation time:" + (System.currentTimeMillis() - timestamp) + "ms");
		timestamp = System.currentTimeMillis();
	}

}
