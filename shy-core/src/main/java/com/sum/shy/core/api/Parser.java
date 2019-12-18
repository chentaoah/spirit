package com.sum.shy.core.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

/**
 * 语义解析命令
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public interface Parser {

	public static Map<String, Parser> parsers = new ConcurrentHashMap<>();

	public static void register(String syntax, Parser parser) {
		parsers.put(syntax, parser);
	}

	public static Parser get(String syntax) {
		return parsers.get(syntax);
	}

	int parse(CtClass mainClass, String scope, List<Line> lines, int index, Line line, Stmt stmt);

}
