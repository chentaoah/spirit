package com.sum.spirit.core.compile;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.api.ClassResolver;
import com.sum.spirit.core.api.Compiler;
import com.sum.spirit.core.api.DocumentReader;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.element.entity.Document;

@Component
@Primary
public class CoreCompiler implements Compiler {

	@Autowired
	public DocumentReader reader;
	@Autowired
	public ClassResolver resolver;

	@Override
	public Map<String, IClass> compile(String name, InputStream input, String... arguments) {
		Document document = reader.read(TypeUtils.getLastName(name), input);
		Map<String, IClass> classes = resolver.resolve(TypeUtils.getPackage(name), document);
		return classes;
	}

}
