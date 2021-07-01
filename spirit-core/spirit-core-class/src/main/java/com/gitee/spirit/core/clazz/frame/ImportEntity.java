package com.gitee.spirit.core.clazz.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gitee.spirit.common.enums.PrimitiveEnum;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.common.utils.SpringUtils;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.api.ImportSelector;
import com.gitee.spirit.core.clazz.entity.IAnnotation;
import com.gitee.spirit.core.clazz.entity.Import;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.core.element.entity.Element;

import cn.hutool.core.lang.Assert;

public abstract class ImportEntity extends AnnotationEntity {

    public List<Import> imports;

    public ImportEntity(List<Import> imports, List<IAnnotation> annotations, Element element) {
        super(annotations, element);
        this.imports = imports != null ? new ArrayList<>(imports) : new ArrayList<>();
    }

    public List<Import> getImports() {
        return ListUtils.findAll(imports, import0 -> !import0.hasAlias());
    }

    public List<Import> getAliasImports() {
        return ListUtils.findAll(imports, Import::hasAlias);
    }

    public Import findImport(String className) {
        return ListUtils.findOne(imports, import0 -> import0.matchSourceName(className));
    }

    public Import findImportByLastName(String simpleName) {
        return ListUtils.findOne(imports, import0 -> import0.matchLastName(simpleName));
    }

    public String findClassName(String simpleName) {
        // 校验
        Assert.notContain(simpleName, ".", "Simple name cannot contains \".\"");

        // 如果传进来是个数组，那么处理一下
        String targetName = TypeUtils.getTargetName(simpleName);
        boolean isArray = TypeUtils.isArray(simpleName);

        // 1.如果是基本类型，基本类型数组
        String className = PrimitiveEnum.findClassName(simpleName);

        // 2.首先先去引入里面找
        if (className == null) {
            Import import0 = findImportByLastName(targetName);
            className = import0 != null ? import0.getClassName() : null;
            className = className != null ? TypeUtils.getClassName(isArray, className) : null;
        }

        // 3.使用类加载器，进行查询
        if (className == null) {
            className = findClassNameByLoader(targetName);
            className = className != null ? TypeUtils.getClassName(isArray, className) : null;
        }

        Assert.notNull(className, "No import info found!simpleName:[" + simpleName + "]");
        return className;
    }

    public boolean addImport(String className) {
        // 如果是数组，则把修饰符号去掉
        String targetName = TypeUtils.getTargetName(className);
        String lastName = TypeUtils.getLastName(className);

        // 1. 原始类型不添加
        if (PrimitiveEnum.isPrimitive(targetName)) {
            return true;
        }

        // 2.如果引入了，则不必引入了
        Import import0 = findImport(targetName);
        if (import0 != null) {
            return !import0.hasAlias();
        }

        // 3.如果存在简称相同的，则也不能引入
        Import import1 = findImportByLastName(lastName);
        if (import1 != null) {
            return false;
        }

        // 4.基础类型或拓展类型不添加
        if (!shouldImport(getClassName(), targetName)) {
            return true;
        }

        // 构建一个行元素
        ElementBuilder builder = SpringUtils.getBean(ElementBuilder.class);
        imports.add(new Import(builder.build("import " + targetName)));
        return true;
    }

    public String findClassNameByLoader(String simpleName) {
        List<ImportSelector> selectors = SpringUtils.getBeans(ImportSelector.class);
        return ListUtils.collectOne(selectors, selector -> selector.findClassName(simpleName));
    }

    public boolean shouldImport(String selfClassName, String className) {
        List<ImportSelector> selectors = SpringUtils.getBeans(ImportSelector.class);
        Boolean flag = ListUtils.collectOne(selectors, selector -> selector.canHandle(className), selector -> selector.shouldImport(selfClassName, className));
        return flag == null || flag;
    }

    public abstract String getClassName();

}
