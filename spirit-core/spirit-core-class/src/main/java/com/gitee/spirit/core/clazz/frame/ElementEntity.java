package com.gitee.spirit.core.clazz.frame;

import com.gitee.spirit.core.element.entity.Element;

public abstract class ElementEntity extends TypeEntity {

    public Element element;

    public ElementEntity(Element element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return element.toString();
    }

}
