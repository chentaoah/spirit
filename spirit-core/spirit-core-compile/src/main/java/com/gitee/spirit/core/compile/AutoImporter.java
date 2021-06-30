package com.gitee.spirit.core.compile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.api.SemanticParser;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.TypeVisitor;
import com.gitee.spirit.core.element.entity.Element;

@Component
public class AutoImporter {

    public static final String STRING_REGEX = "(?<=\").*?(?=\")";// 把字符串都替换掉
    public static final Pattern TYPE_PATTERN = Pattern.compile("(\\b[A-Z]+\\w+\\b)");// 间接排除了泛型类型T

    @Autowired
    public ElementBuilder builder;
    @Autowired
    public SemanticParser parser;

    public void autoImport(IClass clazz) {
        Set<String> classNames = dependencies(clazz);
        classNames.forEach(clazz::addImport);
    }

    /**
     * 这里不使用document对象的原因是，它包含多个类型，扫描之后，无法区分一个引用类型是归属哪个类的
     */
    public Set<String> dependencies(IClass clazz) {
        Set<String> classNames = new HashSet<>();
        // 将注解转成字符串，并重新构建Element对象
        List<Element> elements = new ArrayList<>();
        clazz.annotations.forEach((annotation) -> elements.add(builder.build(annotation.toString())));
        visitElements(clazz, classNames, elements);
        // 遍历class内容
        visitElements(clazz, classNames, Collections.singletonList(clazz.element));
        // 排除了自身
        classNames.remove(clazz.getClassName());
        return classNames;
    }

    public void visitElements(IClass clazz, Set<String> classNames, List<Element> elements) {
        for (Element element : elements) {
            String line = element.line.text;
            // 剔除掉字符串
            line = line.replaceAll(STRING_REGEX, "").trim();
            // 进行正则匹配
            Matcher matcher = TYPE_PATTERN.matcher(line);
            while (matcher.find() && matcher.groupCount() > 0) {
                String targetName = matcher.group(matcher.groupCount() - 1);
                if (parser.isType(targetName)) {
                    String className = clazz.findClassName(targetName);
                    classNames.add(className);
                }
            }
            // 处理子节点
            if (element.hasChild()) {
                visitElements(clazz, classNames, element.children);
            }
        }
    }

    public String getFinalName(IClass clazz, IType type) {
        return TypeVisitor.forEachTypeName(type, eachType -> {
            if (!clazz.addImport(eachType.getClassName())) {
                return eachType.getTypeName();
            }
            return eachType.toString();
        });
    }

}
