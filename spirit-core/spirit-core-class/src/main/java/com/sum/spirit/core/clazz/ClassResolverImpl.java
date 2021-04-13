package com.sum.spirit.core.clazz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.core.api.ClassResolver;
import com.sum.spirit.core.api.ElementBuilder;
import com.sum.spirit.core.api.ImportSelector;
import com.sum.spirit.core.api.TypeFactory;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IField;
import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.element.entity.Document;
import com.sum.spirit.core.element.entity.Element;

@Component
public class ClassResolverImpl implements ClassResolver {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public List<ImportSelector> importSelectors;
	@Autowired
	public ElementBuilder builder;

	@Override
	public Map<String, IClass> resolve(String packageStr, Document document) {

		Map<String, IClass> classes = new LinkedHashMap<>();
		List<Import> imports = new ArrayList<>();
		List<IAnnotation> annotations = new ArrayList<>();
		List<IField> fields = new ArrayList<>();
		List<IMethod> methods = new ArrayList<>();
		IClass mainClass = null;

		for (Element element : document) {
			if (element.isImport()) {
				imports.add(new Import(element));

			} else if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element.get(0)));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				element.addModifiers(KeywordEnum.PUBLIC.value, KeywordEnum.STATIC.value);
				fields.add(new IField(copyAnnotations(annotations), element));

			} else if (element.isDeclareFunc() || element.isFunc()) {
				element.addModifiers(KeywordEnum.PUBLIC.value, KeywordEnum.STATIC.value);
				methods.add(new IMethod(copyAnnotations(annotations), element));

			} else if (element.isInterface() || element.isAbstract()) {
				// 接口和抽象类，只允许出现一个主类
				mainClass = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
				mainClass.factory = factory;
				mainClass.importSelectors = importSelectors;
				mainClass.packageStr = packageStr;
				mainClass.fields = fields;
				mainClass.methods = methods;
				readRootElement(mainClass);
				classes.put(mainClass.getClassName(), mainClass);

			} else if (element.isClass()) {
				// 这里可能出现泛型，但是文件名一般是simpleName
				String simpleName = element.getKeywordParam(KeywordEnum.CLASS.value).toString();
				String targetName = TypeUtils.getTargetName(simpleName);
				if (document.fileName.equals(targetName)) {
					mainClass = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
					mainClass.factory = factory;
					mainClass.importSelectors = importSelectors;
					mainClass.packageStr = packageStr;
					mainClass.fields = fields;
					mainClass.methods = methods;
					readRootElement(mainClass);
					classes.put(mainClass.getClassName(), mainClass);

				} else {
					IClass clazz = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
					clazz.factory = factory;
					clazz.importSelectors = importSelectors;
					clazz.packageStr = packageStr;
					clazz.fields = new ArrayList<>();
					clazz.methods = new ArrayList<>();
					readRootElement(clazz);
					classes.put(clazz.getClassName(), clazz);
				}
			}
		}

		// 如果不存在主类的声明，则虚拟一个Element
		if (mainClass == null) {
			Element element = builder.build("class " + document.fileName + " {");
			mainClass = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
			mainClass.factory = factory;
			mainClass.importSelectors = importSelectors;
			mainClass.packageStr = packageStr;
			mainClass.fields = fields;
			mainClass.methods = methods;
			classes.put(mainClass.getClassName(), mainClass);
		}

		return classes;
	}

	public List<IAnnotation> copyAnnotations(List<IAnnotation> annotations) {
		List<IAnnotation> newAnnotations = new ArrayList<>(annotations);
		annotations.clear();
		return newAnnotations;
	}

	public void readRootElement(IClass clazz) {
		List<IAnnotation> annotations = new ArrayList<>();
		for (Element element : clazz.element.children) {
			if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element.get(0)));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				clazz.fields.add(new IField(copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value)));

			} else if (element.isDeclareFunc() || element.isFunc()) {
				clazz.methods.add(new IMethod(copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value)));
			}
		}
	}

}
