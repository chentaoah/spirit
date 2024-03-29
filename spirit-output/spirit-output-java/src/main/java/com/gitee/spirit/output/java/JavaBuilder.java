package com.gitee.spirit.output.java;

import java.util.List;
import java.util.Map;

import com.gitee.spirit.output.api.ImportManager;
import com.gitee.spirit.output.java.entity.StaticImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.api.CodeBuilder;
import com.gitee.spirit.core.api.ElementAction;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IField;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.Import;
import com.gitee.spirit.core.clazz.frame.MemberEntity;
import com.gitee.spirit.core.compile.AutoImporter;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.output.java.action.AbstractExtElementAction;

@Component
public class JavaBuilder implements CodeBuilder {

    public static final String IMPLEMENTS_KEYWORD = "implements";
    public static final String SYNCHRONIZED_KEYWORD = "synchronized";
    public static final String FINAL_KEYWORD = "final";

    @Autowired
    public ImportManager manager;
    @Autowired
    public AutoImporter importer;
    @Autowired
    public List<AbstractExtElementAction> actions;

    @Override
    public String build(IClass clazz) {
        String body = buildBody(clazz);
        String head = buildHead(clazz);
        return head + body;
    }

    public String buildHead(IClass clazz) {
        StringBuilder builder = new StringBuilder();
        // package
        builder.append(String.format("package %s;\n\n", clazz.packageStr));
        // import
        List<Import> imports = clazz.getImports();
        imports.forEach((imp) -> builder.append(imp.element).append(";\n"));
        // static import
        List<StaticImport> staticImports = manager.getStaticImports(clazz);
        staticImports.forEach((imp) -> builder.append(imp.element).append(";\n"));
        if (imports.size() > 0 || staticImports.size() > 0) {
            builder.append("\n");
        }
        // annotation
        clazz.annotations.forEach((annotation) -> builder.append(annotation).append("\n"));
        return builder.toString();
    }

    public String buildBody(IClass clazz) {
        StringBuilder classStr = new StringBuilder();
        // 处理一部分关键字
        clazz.element.insertKeywordAfter(KeywordEnum.ABSTRACT.value, KeywordEnum.CLASS.value);
        clazz.element.replaceKeyword(KeywordEnum.IMPLS.value, JavaBuilder.IMPLEMENTS_KEYWORD);
        classStr.append(clazz.element).append("\n\n");
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
            field.annotations.forEach((annotation) -> fieldsStr.append("\t").append(annotation).append("\n"));
            field.element.replaceModifier(KeywordEnum.CONST.value, JavaBuilder.FINAL_KEYWORD);
            fieldsStr.append("\t").append(convert(clazz, field, field.element)).append("\n");
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
            method.annotations.forEach((annotation) -> methodsStr.append("\t").append(annotation).append("\n"));

            Element element = method.element;
            // 静态主方法
            if (method.isStatic() && "main".equals(method.getName())) {
                methodsStr.append("\tpublic static void main(String[] args) {\n");

            } else {// public User() // public static synchronized String methodName()
                // 替换关键字
                element.replaceModifier(KeywordEnum.SYNCH.value, JavaBuilder.SYNCHRONIZED_KEYWORD);
                if (element.isDeclareFunc()) {
                    // 抽象类型的没有方法体的方法，需要加上abstract关键字
                    if (clazz.isAbstract() && !method.isStatic() && !element.hasChild()) {
                        element.insertModifier(KeywordEnum.PUBLIC.value, KeywordEnum.ABSTRACT.value);
                    }

                    if (element.hasChild()) {
                        methodsStr.append("\t").append(element).append("\n");
                    } else {
                        methodsStr.append("\t").append(element).append(";\n\n");
                    }

                } else if (element.isFunc()) {
                    if (method.isInit()) {
                        element.removeKeyword(KeywordEnum.FUNC.value);
                    } else {
                        element.replaceKeyword(KeywordEnum.FUNC.value, importer.getFinalName(clazz, method.getType()));
                    }
                    methodsStr.append("\t").append(element).append("\n");
                }
            }
            // 方法体可能没有内容，但是这并不意味着这个方法没有被实现
            if (element.hasChild()) {
                convertMethodElement(methodsStr, "\t\t", clazz, method, method.element);
                methodsStr.append("\t}\n\n");
            }
        }
        return methodsStr.toString();
    }

    public void convertMethodElement(StringBuilder builder, String indent, IClass clazz, IMethod method, Element father) {
        for (Element element : father.children) {
            builder.append(indent).append(convert(clazz, method, element)).append("\n");
            if (element.hasChild()) {
                convertMethodElement(builder, indent + "\t", clazz, method, element);
            }
        }
    }

    public Element convert(IClass clazz, MemberEntity member, Element element) {
        for (ElementAction action : actions) {
            action.visitElement(new VisitContext(clazz, member), element);
        }
        return element;
    }

}
