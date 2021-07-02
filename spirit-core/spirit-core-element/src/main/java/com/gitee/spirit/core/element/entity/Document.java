package com.gitee.spirit.core.element.entity;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.element.frame.Syntactic;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Document extends ArrayList<Element> {

    public String fileName;

    public List<Element> findElements(Element element) {
        int index = ListUtils.indexOf(this, element::equals);
        if (index >= 0) {
            int startIndex = ListUtils.indexOfUnmatched(this, index - 1, -1, Syntactic::isAnnotation);
            int endIndex = ListUtils.indexOf(this, index + 1, size(), Syntactic::isEnd);
            return new ArrayList<>(subList(startIndex + 1, endIndex + 1));
        }
        throw new RuntimeException("The element was not found!");
    }

    public void debug() {
        for (Element element : this) {
            element.debug();
        }
    }

}
