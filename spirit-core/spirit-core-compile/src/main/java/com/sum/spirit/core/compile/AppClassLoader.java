package com.sum.spirit.core.compile;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.FileURLUtils;
import com.sum.spirit.core.api.Compiler;
import com.sum.spirit.core.clazz.entity.IClass;

@Component
@Order(-100)
@DependsOn("configUtils")
public class AppClassLoader extends AbstractURLClassLoader {

	@Autowired
	public Compiler compiler;
	@Autowired
	public AutoImporter importer;
	@Autowired
	public ClassVisiter visiter;

	@Override
	public List<IClass> getAllClasses() {
		if (super.getAllClasses().size() == 0) {
			List<String> names = getNames();
			names.forEach(name -> findClass(name));
			List<IClass> classes = super.getAllClasses();
			visitClasses(classes);
			return classes;
		}
		return super.getAllClasses();
	}

	@Override
	public IClass defineClass(String name, URL resource) {
		Map<String, IClass> classes = compiler.compile(name, FileURLUtils.asStream(resource));
		this.classes.putAll(classes);
		return classes.get(name);
	}

	public void visitClasses(List<IClass> classes) {
		classes.forEach(clazz -> importer.autoImport(clazz));
		classes.forEach(clazz -> visiter.prepareForVisit(clazz));
		classes.forEach(clazz -> visiter.visitClass(clazz));
	}

}
