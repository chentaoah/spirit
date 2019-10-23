package com.sum.shy.core;

import java.io.File;
import java.io.IOException;

import com.sum.shy.api.ClassReader;
import com.sum.shy.api.CodeBuilder;
import com.sum.shy.builder.JavaBuilder;
import com.sum.shy.clazz.Clazz;

public class Compiler {

	// 主方法
	public static void main(String[] args) throws IOException {

		ClassReader reader = new ShyReader();
		CodeBuilder builder = new JavaBuilder();
		// 1.读取文件
		String path = "C:\\Users\\chentao26275\\Desktop\\User.shy";
		File dir = new File(path);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				Clazz clazz = reader.readFile(file);
				String text = builder.build(clazz);
				System.out.println(text);
			}
		} else if (dir.isFile()) {
			Clazz clazz = reader.readFile(dir);
			String text = builder.build(clazz);
			System.out.println(text);
		}

	}

}
