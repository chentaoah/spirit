package com.sum.shy.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.shy.clazz.Clazz;
import com.sum.shy.sentence.Sentence;

public interface Command {

	public static Map<String, Command> commands = new ConcurrentHashMap<>();

	public static void register(String keyword, Command command) {
		commands.put(keyword, command);
	}

	public static Command get(String keyword) {
		return commands.get(keyword);
	}

	int handle(String scope, Clazz clazz, List<String> lines, int index, Sentence sentence);

}
