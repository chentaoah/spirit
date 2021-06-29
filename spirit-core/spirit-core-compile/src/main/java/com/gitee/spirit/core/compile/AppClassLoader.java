package com.gitee.spirit.core.compile;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.utils.URLFileUtils;
import com.gitee.spirit.core.api.ClassVisiter;
import com.gitee.spirit.core.api.Compiler;
import com.gitee.spirit.core.clazz.AbstractURLClassLoader;
import com.gitee.spirit.core.clazz.entity.IClass;

@Component
@Order(-100)
@DependsOn("configUtils")
public class AppClassLoader extends AbstractURLClassLoader {

    @Autowired
    public Compiler compiler;
    @Autowired
    public AutoImporter importer;
    @Autowired
    public ClassVisiter visitor;

    @Override
    public List<IClass> getAllClasses() {
        if (super.getAllClasses().size() == 0) {
            List<String> names = getNames();
            names.forEach(this::loadClass);
            List<IClass> classes = super.getAllClasses();
            visitClasses(classes);
            return classes;
        }
        return super.getAllClasses();
    }

    @Override
    public IClass defineClass(String name, URL resource) {
        Map<String, IClass> classes = compiler.compile(name, URLFileUtils.asStream(resource));
        this.classes.putAll(classes);
        return classes.get(name);
    }

    public void visitClasses(List<IClass> classes) {
        classes.forEach(clazz -> importer.autoImport(clazz));
        classes.forEach(clazz -> visitor.prepareForVisit(clazz));
        classes.forEach(clazz -> visitor.visitClass(clazz));
    }

}
