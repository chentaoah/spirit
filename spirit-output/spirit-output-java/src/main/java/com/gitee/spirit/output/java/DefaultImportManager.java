package com.gitee.spirit.output.java;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.output.api.ImportManager;
import com.gitee.spirit.output.java.entity.StaticImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultImportManager implements ImportManager {

    public Map<IClass, List<StaticImport>> classStaticImportsMap = new ConcurrentHashMap<>();
    @Autowired
    public ElementBuilder builder;

    @Override
    public List<StaticImport> getStaticImports(IClass clazz) {
        return classStaticImportsMap.computeIfAbsent(clazz, clazz1 -> new ArrayList<>());
    }

    @Override
    public StaticImport findStaticImport(IClass clazz, String sourceName) {
        List<StaticImport> staticImports = getStaticImports(clazz);
        return ListUtils.findOne(staticImports, staticImport -> staticImport.matchSourceName(sourceName));
    }

    @Override
    public boolean addStaticImport(IClass clazz, String sourceName) {
        StaticImport staticImport = findStaticImport(clazz, sourceName);
        if (staticImport != null) {
            return true;
        }
        List<StaticImport> staticImports = getStaticImports(clazz);
        staticImports.add(new StaticImport(builder.build("import static " + sourceName)));
        return true;
    }


}
