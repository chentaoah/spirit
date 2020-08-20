package com.sum.spirit.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;

@Service("compiler")
public interface Compiler {

	Map<String, IClass> compile(Map<String, File> files);

}
