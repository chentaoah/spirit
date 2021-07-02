package com.gitee.spirit.core.clazz.entity;

import java.util.ArrayList;
import java.util.List;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.common.utils.SpringUtils;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.frame.ImportableEntity;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Token;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import cn.hutool.core.lang.Assert;

public class IClass extends ImportableEntity {

    public String packageStr;
    public List<IField> fields;
    public List<IMethod> methods;

    public IClass(List<Import> imports, List<IAnnotation> annotations, Element element) {
        super(imports, annotations, element);
    }

    public boolean isInterface() {
        return element.isInterface();
    }

    public boolean isAbstract() {
        return element.isAbstract();
    }

    public boolean isClass() {
        return element.isClass();
    }

    public Token getTypeToken() {
        Assert.isTrue(isInterface() || isAbstract() || isClass(), "The class type is wrong!");
        Token typeToken = element.getKeywordParam(KeywordEnum.INTERFACE.value, KeywordEnum.CLASS.value, KeywordEnum.ABSTRACT.value);
        Assert.isTrue(typeToken != null && typeToken.isType(), "Cannot get type token of class!");
        return typeToken;
    }

    public String getSimpleName() {
        return TypeUtils.getTargetName(getTypeToken().toString());
    }

    public String getClassName() {
        return packageStr + "." + getSimpleName();
    }

    public int getTypeVariableIndex(String genericName) {// 这样分割，是有风险的，不过一般来说，类型说明里面不会再有嵌套
        List<String> names = Splitter.on(CharMatcher.anyOf("<,>")).trimResults().splitToList(getTypeToken().toString());
        int index = ListUtils.indexOf(names, 1, names.size(), name -> name.equals(genericName));
        return index >= 1 ? index - 1 : -1;
    }

    public IType getSuperType() {// 注意:这里返回的是Super<T,K>
        Token token = element.getKeywordParam(KeywordEnum.EXTENDS.value);// 这里返回的,可以是泛型格式，而不是className
        if (token != null) {
            TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
            return factory.create(this, token);
        }
        return null;
    }

    public List<IType> getInterfaceTypes() {
        List<IType> interfaces = new ArrayList<>();
        TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
        for (Token token : element.getKeywordParams(KeywordEnum.IMPLS.value)) {
            interfaces.add(factory.create(this, token));
        }
        return interfaces;
    }

    public IField getField(String fieldName) {
        return ListUtils.findOne(fields, field -> field.getName().equals(fieldName));
    }

    public List<IMethod> getMethods(String methodName) {
        return ListUtils.findAll(methods, method -> method.getName().equals(methodName));
    }

}
