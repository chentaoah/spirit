package com.sum.shy.core;

import java.io.File;
import java.io.IOException;

import com.sum.shy.core.entity.Clazz;

public class Compiler {

	// 主方法
	public static void main(String[] args) throws IOException {

//		boolean debug = false;
		boolean debug = true;
		String path = null;

		String OSName = System.getProperty("os.name");
		switch (OSName) {
		case "Windows 10":
			path = "D:\\Work\\CloudSpace\\Shy\\shy-core\\src\\main\\resources\\User.shy";
			break;
		case "Mac OS X":
			path = "/Users/chentao/Work/CloudSpace/Shy/shy-core/src/main/resources/User.shy";
			break;
		default:
			path = "D:\\Work\\CloudSpace\\Shy\\shy-core\\src\\main\\resources\\User.shy";
			break;
		}

		File file = new File(path);
		if (!debug) {
			Clazz clazz = new ShyReader().read(file);
			String text = new JavaBuilder().build(clazz);
			System.out.println(text);
		} else {
			new ShyDebugger().read(file);
		}

	}

}
