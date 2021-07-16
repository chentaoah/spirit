package com.gitee.spirit.core.clazz.entity;

import java.util.List;

import cn.hutool.core.lang.Assert;
import com.gitee.spirit.core.clazz.frame.AnnotatedEntity;
import com.gitee.spirit.core.clazz.frame.NamedEntity;
import com.gitee.spirit.core.element.entity.Element;

public class IParameter extends AnnotatedEntity implements NamedEntity {

    public IParameter(List<IAnnotation> annotations, Element element) {
        super(annotations, element);
    }

    @Override
    public String getName() {
        Assert.isTrue(element.isDeclare(), "Unsupported syntax!syntax:" + element.syntax);
        return element.getStr(1);
    }

    @Override
    public String toString() {
        return element.toString();
    }

}
