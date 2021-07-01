package com.gitee.spirit.output.java.entity;

import com.gitee.spirit.core.clazz.entity.Import;
import com.gitee.spirit.core.element.entity.Element;

public class StaticImport extends Import {

    public StaticImport(Element element) {
        super(element);
    }

    @Override
    public boolean matchSourceName(String sourceName) {
        return element.getStr(2).equals(sourceName);
    }

}
