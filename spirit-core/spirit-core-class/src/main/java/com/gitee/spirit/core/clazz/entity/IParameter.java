package com.gitee.spirit.core.clazz.entity;

import java.util.List;

import com.gitee.spirit.core.clazz.frame.AnnotatedEntity;
import com.gitee.spirit.core.clazz.frame.NamedEntity;
import com.gitee.spirit.core.element.entity.Element;

public class IParameter extends AnnotatedEntity implements NamedEntity {

    public IParameter(List<IAnnotation> annotations, Element element) {
        super(annotations, element);
    }

    @Override
    public String getName() {
        if (element.isDeclare()) {
            return element.getStr(1);
        }
        throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
    }

    @Override
    public String toString() {
        return element.toString();
    }

}
