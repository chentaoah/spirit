package com.sum.spirit.core;

import org.springframework.stereotype.Component;

@Component
public class RunningMonitor {

	public void printArgs(String[] sourceArgs) {
		for (String arg : sourceArgs) {
			System.out.println(arg);
		}
		System.out.println("");
	}

	public void printTotalTime(long timestamp) {
		System.out.println("Total time: " + (System.currentTimeMillis() - timestamp) + "ms");
	}

}
