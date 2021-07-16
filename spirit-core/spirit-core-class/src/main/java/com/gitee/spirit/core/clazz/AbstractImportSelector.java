package com.gitee.spirit.core.clazz;

import com.gitee.spirit.core.api.ImportSelector;
import com.gitee.spirit.core.clazz.utils.TypeUtils;

public abstract class AbstractImportSelector implements ImportSelector {

    @Override
    public boolean shouldImport(String selfClassName, String className) {
        return !(selfClassName.equals(className) || TypeUtils.isSamePackage(selfClassName, className));
    }

}
