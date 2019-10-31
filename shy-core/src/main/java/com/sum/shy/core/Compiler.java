package com.sum.shy.core;

import java.io.File;
import java.io.IOException;

import com.sum.shy.core.entity.Clazz;

public class Compiler {

	// 主方法
	public static void main(String[] args) throws IOException {

		// 1.读取文件
//		String path = "/Users/chentao/Work/CloudSpace/Shy/shy-core/src/main/resources/User.shy";
		String path = "D:\\Work\\CloudSpace\\Shy\\shy-core\\src\\main\\resources\\User.shy";
		File file = new File(path);
		Clazz clazz = new ShyReader().read(file);
		String text = new JavaBuilder().build(clazz);
		System.out.println(text);
	}

}
