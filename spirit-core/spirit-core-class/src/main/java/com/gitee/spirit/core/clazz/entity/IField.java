package com.gitee.spirit.core.clazz.entity;

import java.util.List;

import cn.hutool.core.lang.Assert;
import com.gitee.spirit.core.clazz.frame.MemberEntity;
import com.gitee.spirit.core.element.entity.Element;

public class IField extends MemberEntity {

    public IField(List<IAnnotation> annotations, Element element) {
        super(annotations, element);
    }

    @Override
    public String getName() {
        if (element.isDeclare() || element.isDeclareAssign()) {
            return element.getStr(1);

        } else if (element.isAssign()) {
            return element.getStr(0);
        }
        throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
    }

}
