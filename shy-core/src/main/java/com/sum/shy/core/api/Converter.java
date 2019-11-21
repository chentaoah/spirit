package com.sum.shy.core.api;

import java.util.List;
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

	public int convert(StringBuilder sb, String block, String indent, CtClass clazz, CtMethod method, List<Line> lines,
			int index, Line line, Stmt stmt);

}
