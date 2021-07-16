package com.gitee.spirit.core.compile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gitee.spirit.core.api.ClassResolver;
import com.gitee.spirit.core.element.entity.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public ClassResolver resolver;
    @Autowired
    public SemanticParser parser;

    public void autoImport(IClass clazz) {
        Set<String> classNames = dependencies(clazz);
        classNames.forEach(clazz::addImport);
    }

    public Set<String> dependencies(IClass clazz) {
        Document document = resolver.findDocument(clazz);
        List<Element> elements = document.findElements(clazz.element);
        Set<String> classNames = new HashSet<>();
        visitElements(clazz, elements, classNames);
        classNames.remove(clazz.getClassName());// 排除自身
        return classNames;
    }

    public void visitElements(IClass clazz, List<Element> elements, Set<String> classNames) {
        for (Element element : elements) {
            String line = element.line.text;
            line = line.replaceAll(STRING_REGEX, "").trim(); // 剔除掉字符串
            Matcher matcher = TYPE_PATTERN.matcher(line); // 进行正则匹配
            while (matcher.find() && matcher.groupCount() > 0) {
                String targetName = matcher.group(matcher.groupCount() - 1);
                if (parser.isType(targetName)) {
                    String className = clazz.findClassName(targetName);
                    classNames.add(className);
                }
            }
            if (element.hasChild()) {// 处理子节点
                visitElements(clazz, element.children, classNames);
            }
        }
    }

    public String getFinalName(IClass clazz, IType type) {
        return TypeVisitor.forEachTypeName(type, eachType -> !clazz.addImport(eachType.getClassName()) ? eachType.getTypeName() : eachType.toString());
    }

}
