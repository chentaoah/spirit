package com.sum.shy.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;

@Service("compiler")
public interface Compiler {

	Map<String, IClass> compile(Map<String, File> files);

}
