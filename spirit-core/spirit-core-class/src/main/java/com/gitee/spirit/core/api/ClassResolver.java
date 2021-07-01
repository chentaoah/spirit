package com.gitee.spirit.core.api;

import java.util.Map;

import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.element.entity.Document;

public interface ClassResolver {

    Map<String, IClass> resolve(String packageStr, Document document);

    Document getDocument(IClass clazz);

}
