package com.gitee.spirit.output.api;

import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.output.java.entity.StaticImport;

import java.util.List;

public interface ImportManager {

    List<StaticImport> getStaticImports(IClass clazz);

    StaticImport findStaticImport(IClass clazz, String sourceName);

    boolean addStaticImport(IClass clazz, String sourceName);

}
