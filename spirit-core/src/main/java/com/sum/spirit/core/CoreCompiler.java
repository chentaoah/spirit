package com.sum.spirit.core;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.core.clazz.ClassResolver;
import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.element.DocumentReader;
import com.sum.spirit.core.element.pojo.Document;
import com.sum.spirit.utils.TypeUtils;

@Component
@Primary
public class CoreCompiler implements Compiler {

	@Autowired
	public DocumentReader reader;
	@Autowired
	public ClassResolver resolver;

	@Override
	public Map<String, IClass> compile(String name, InputStream input, String... arguments) {
		Document document = reader.readDocument(TypeUtils.getLastName(name), input);
		return resolver.resolveClasses(TypeUtils.getPackage(name), document);
	}

}
