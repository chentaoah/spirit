package com.sum.spirit.core;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class RunningMonitor {

	public void printArgs(Map<String, Object> args) {
		for (Entry<String, Object> entry : args.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		System.out.println("");
	}

	public void printTotalTime(long timestamp) {
		System.out.println("Total time: " + (System.currentTimeMillis() - timestamp) + "ms");
	}

}
