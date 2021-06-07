package com.gitee.spirit.core.clazz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.common.utils.GlobalContext;
import com.gitee.spirit.core.api.ClassResolver;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.clazz.entity.IAnnotation;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IField;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.Import;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.core.element.entity.Document;
import com.gitee.spirit.core.element.entity.Element;

@Component
public class DefaultClassResolver implements ClassResolver {

	@Autowired
	public ElementBuilder builder;
	@Autowired
	public GlobalContext context;

	@Override
	public Map<String, IClass> resolve(String packageStr, Document document) {

		Map<String, IClass> classes = new LinkedHashMap<>();

		List<Import> imports = new ArrayList<>();
		List<IAnnotation> annotations = new ArrayList<>();
		IClass mainClass = null;
		List<IField> fields = new ArrayList<>();
		List<IMethod> methods = new ArrayList<>();

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
				initClass(mainClass, packageStr, fields, methods);
				readRootElement(mainClass);
				classes.put(mainClass.getClassName(), mainClass);

			} else if (element.isClass()) {
				// 这里可能出现泛型，但是文件名一般是simpleName
				String simpleName = element.getKeywordParam(KeywordEnum.CLASS.value).toString();
				// 声明一个类型时，泛型参数不能多层嵌套
				String targetName = TypeUtils.getTargetName(simpleName);
				if (document.fileName.equals(targetName)) {
					mainClass = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
					initClass(mainClass, packageStr, fields, methods);
					readRootElement(mainClass);
					classes.put(mainClass.getClassName(), mainClass);

				} else {
					IClass clazz = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
					initClass(clazz, packageStr, new ArrayList<>(), new ArrayList<>());
					readRootElement(clazz);
					classes.put(clazz.getClassName(), clazz);
				}
			}
		}

		// 如果不存在主类的声明，则虚拟一个Element
		if (mainClass == null) {
			Element element = builder.build("class " + document.fileName + " {");
			mainClass = new IClass(imports, copyAnnotations(annotations), element.addModifiers(KeywordEnum.PUBLIC.value));
			initClass(mainClass, packageStr, fields, methods);
			classes.put(mainClass.getClassName(), mainClass);
		}

		return classes;
	}

	public List<IAnnotation> copyAnnotations(List<IAnnotation> annotations) {
		List<IAnnotation> newAnnotations = new ArrayList<>(annotations);
		annotations.clear();
		return newAnnotations;
	}

	public void initClass(IClass clazz, String packageStr, List<IField> fields, List<IMethod> methods) {
		clazz.packageStr = packageStr;
		clazz.fields = fields;
		clazz.methods = methods;
		clazz.context = context;
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
