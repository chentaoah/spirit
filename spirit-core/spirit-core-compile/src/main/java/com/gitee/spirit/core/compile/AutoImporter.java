package com.gitee.spirit.core.compile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gitee.spirit.core.api.ClassResolver;
import com.gitee.spirit.core.element.entity.Document;
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

    // 把字符串都替换掉
    public static final String STRING_REGEX = "(?<=\").*?(?=\")";
    // 间接排除了泛型类型T
    public static final Pattern TYPE_PATTERN = Pattern.compile("(\\b[A-Z]+\\w+\\b)");

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
        // 排除自身
        classNames.remove(clazz.getClassName());
        return classNames;
    }

    public void visitElements(IClass clazz, List<Element> elements, Set<String> classNames) {
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
                visitElements(clazz, element.children, classNames);
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
