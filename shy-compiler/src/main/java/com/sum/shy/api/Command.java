package com.sum.shy.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public interface Command {

	public static Map<String, Command> commands = new ConcurrentHashMap<>();

	public static Command get(String keyword) {
		return commands.get(keyword);
	}

	public static void register(String keyword, Command command) {
		commands.put(keyword, command);
	}

	int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line);

}
