package com.sum.spirit.starter.java;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class RunningMonitor {

	public void printArgs(ApplicationArguments args) {
		for (String name : args.getOptionNames()) {
			System.out.println(name + " : " + args.getOptionValues(name));
		}
		System.out.println("");
	}

	public void printTotalTime(long timestamp) {
		System.out.println("Total time: " + (System.currentTimeMillis() - timestamp) + "ms");
	}

}
