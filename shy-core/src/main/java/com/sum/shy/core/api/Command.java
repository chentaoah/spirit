package com.sum.shy.core.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.shy.core.entity.Result;

/**
 * 语义解析命令
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public interface Command {

	public static Map<String, Command> commands = new ConcurrentHashMap<>();

	public static void register(String keyword, Command command) {
		commands.put(keyword, command);
	}

	public static Command get(String keyword) {
		return commands.get(keyword);
	}

	Result analysis(String line, String syntax, List<String> words);

}
