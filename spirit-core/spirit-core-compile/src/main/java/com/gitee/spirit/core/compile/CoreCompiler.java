package com.gitee.spirit.core.compile;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ClassResolver;
import com.gitee.spirit.core.api.Compiler;
import com.gitee.spirit.core.api.DocumentReader;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.core.element.entity.Document;

@Primary
@Component
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
