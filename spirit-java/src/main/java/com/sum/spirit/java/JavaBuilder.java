package com.sum.spirit.java;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.AutoImporter;
import com.sum.spirit.core.api.CodeBuilder;
import com.sum.spirit.core.api.ElementAction;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IField;
import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.visiter.entity.ElementEvent;
import com.sum.spirit.element.entity.Element;

@Component
@DependsOn("springUtils")
public class JavaBuilder implements CodeBuilder, InitializingBean {

	public static final String JAVA_PACKAGE = "com.sum.spirit.java";
	public static final String IMPLEMENTS_KEYWORD = "implements";
	public static final String SYNCHRONIZED_KEYWORD = "synchronized";
	public static final String FINAL_KEYWORD = "final";

	@Autowired
	public AutoImporter importer;

	public List<ElementAction> actions;

	@Override
	public void afterPropertiesSet() throws Exception {
		actions = SpringUtils.getBeansAndSort(ElementAction.class, JAVA_PACKAGE);
	}

	@Override
	public String build(IClass clazz) {
		String body = buildBody(clazz);
		String head = buildHead(clazz);
		return new StringBuilder().append(head).append(body).toString();
	}

	public String buildHead(IClass clazz) {
		StringBuilder builder = new StringBuilder();
		// package
		builder.append(String.format("package %s;\n\n", clazz.packageStr));
		// import
		List<Import> imports = clazz.getImports();
		imports.forEach((imp) -> builder.append(imp.element + ";\n"));
		if (imports.size() > 0) {
			builder.append("\n");
		}
		// annotation
		clazz.annotations.forEach((annotation) -> builder.append(annotation + "\n"));
		return builder.toString();
	}

	public String buildBody(IClass clazz) {
		StringBuilder classStr = new StringBuilder();
		// 处理一部分关键字
		clazz.element.insertKeywordAfter(KeywordEnum.ABSTRACT.value, KeywordEnum.CLASS.value);
		clazz.element.replaceKeyword(KeywordEnum.IMPLS.value, JavaBuilder.IMPLEMENTS_KEYWORD);
		classStr.append(clazz.element + "\n\n");
		// 当构建方法体时，需要动态引入一些类型和字段，所以先构建方法体
		String methodsStr = buildMethods(clazz);
		String fieldsStr = buildFields(clazz);
		classStr.append(fieldsStr).append(methodsStr).append("}\n");
		return classStr.toString();
	}

	public String buildFields(IClass clazz) {
		// fields
		StringBuilder fieldsStr = new StringBuilder();
		// public static type + element
		for (IField field : clazz.fields) {
			// annotation
			field.annotations.forEach((annotation) -> fieldsStr.append("\t" + annotation + "\n"));
			field.element.replaceModifier(KeywordEnum.CONST.value, JavaBuilder.FINAL_KEYWORD);
			fieldsStr.append("\t" + convert(clazz, field.element) + "\n");
		}
		if (fieldsStr.length() > 0) {
			fieldsStr.append("\n");
		}
		return fieldsStr.toString();
	}

	public String buildMethods(IClass clazz) {
		// 当构建方法体时，需要动态引入一些类型和字段，所以先构建方法体
		StringBuilder methodsStr = new StringBuilder();
		// public static type + element
		for (IMethod method : clazz.methods) {
			// annotation
			method.annotations.forEach((annotation) -> methodsStr.append("\t" + annotation + "\n"));

			Element element = method.element;
			// 静态主方法
			if (method.isStatic() && "main".equals(method.getName())) {
				methodsStr.append("\tpublic static void main(String[] args) {\n");

			} else {// public User() // public static synchronized String methodName()
				// 替换关键字
				element.replaceModifier(KeywordEnum.SYNCH.value, JavaBuilder.SYNCHRONIZED_KEYWORD);
				if (element.isFuncDeclare()) {
					// 抽象类型的没有方法体的方法，需要加上abstract关键字
					if (clazz.isAbstract() && !method.isStatic() && !element.hasChild()) {
						element.insertModifier(KeywordEnum.PUBLIC.value, KeywordEnum.ABSTRACT.value);
					}

					if (element.hasChild()) {
						methodsStr.append("\t" + element + "\n");
					} else {
						methodsStr.append("\t" + element + ";\n\n");
					}

				} else if (element.isFunc()) {
					if (method.isInit()) {
						element.removeKeyword(KeywordEnum.FUNC.value);
					} else {
						element.replaceKeyword(KeywordEnum.FUNC.value, importer.getFinalName(clazz, method.getType()));
					}
					methodsStr.append("\t" + element + "\n");
				}
			}
			// 方法体可能没有内容，但是这并不意味着这个方法没有被实现
			if (element.hasChild()) {
				convertMethodElement(methodsStr, "\t\t", clazz, method.element);
				methodsStr.append("\t}\n\n");
			}
		}
		return methodsStr.toString();
	}

	public void convertMethodElement(StringBuilder builder, String indent, IClass clazz, Element father) {
		for (Element element : father.children) {
			builder.append(indent + convert(clazz, element) + "\n");
			if (element.hasChild()) {
				convertMethodElement(builder, indent + "\t", clazz, element);
			}
		}
	}

	public Element convert(IClass clazz, Element element) {
		for (ElementAction action : actions) {
			ElementEvent event = new ElementEvent(clazz, element);
			if (action.isTrigger(event)) {
				action.visit(event);
			}
		}
		return element;
	}

}
