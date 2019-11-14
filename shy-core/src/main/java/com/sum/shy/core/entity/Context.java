package com.sum.shy.core.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Context {

	public static ThreadLocal<Context> local = new ThreadLocal<>();

	public static Context get() {
		if (local.get() == null) {
			local.set(new Context());
		}
		return local.get();
	}

	// 是否debug模式
	public boolean debug;
	// 所有获取到的文件
	public Map<String, File> files;
	// class实体
	public Clazz clazz;
	// 当前域
	public String scope;
	// 方法解析链,不能相互嵌套依赖
	public List<String> dependencies = new ArrayList<>();

	public void addDependency(String dependency) {
		if (dependencies.contains(dependency)) {
			throw new RuntimeException("The current dependency is circular!dependency:" + dependency);
		}
		dependencies.add(dependency);
	}

	public void removeDependency(String dependency) {
		dependencies.remove(dependency);
	}

}
