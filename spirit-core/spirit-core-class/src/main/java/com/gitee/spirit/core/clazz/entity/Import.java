package com.gitee.spirit.core.clazz.entity;

import com.gitee.spirit.core.clazz.frame.ElementEntity;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.core.element.entity.Element;

public class Import extends ElementEntity {

    public Import(Element element) {
        super(element);
    }

    public String getClassName() {
        return element.getStr(1);
    }

    public String getLastName() {
        return TypeUtils.getLastName(getClassName());
    }

    public boolean hasAlias() {
        return element.contains(2);
    }

    public String getAlias() {
        return hasAlias() ? element.getStr(2) : null;
    }

    public boolean matchSourceName(String sourceName) {
        return getClassName().equals(sourceName);
    }

    public boolean matchLastName(String lastName) {
        return !hasAlias() ? getLastName().equals(lastName) : getAlias().equals(lastName);
    }

}
