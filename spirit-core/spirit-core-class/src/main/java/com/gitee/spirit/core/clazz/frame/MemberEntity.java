package com.gitee.spirit.core.clazz.frame;

import java.util.List;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.clazz.entity.IAnnotation;
import com.gitee.spirit.core.element.entity.Element;

public abstract class MemberEntity extends AnnotatedEntity implements NamedEntity {

    public MemberEntity(List<IAnnotation> annotations, Element element) {
        super(annotations, element);
    }

    public boolean isStatic() {
        return element.isModified(KeywordEnum.STATIC.value);
    }

}
