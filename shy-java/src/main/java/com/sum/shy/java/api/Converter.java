package com.sum.shy.java.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Stmt;

public interface Converter {

	public static Map<String, Converter> converters = new ConcurrentHashMap<>();

	public static void register(String syntax, Converter converter) {
		converters.put(syntax, converter);
	}

	public static Converter get(String syntax) {
		return converters.get(syntax);
	}

	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt);

}
