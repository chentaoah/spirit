package com.sum.shy.start;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.core.ShyCompiler;
import com.sum.shy.core.ShyDebugger;
import com.sum.shy.core.proc.AliasReplacer;
import com.sum.shy.java.JavaBuilder;
import com.sum.shy.utils.FileUtils;

/**
 * 将指定shy代码,转换成java代码
 * 
 * @author chentao26275
 *
 */
public class JavaStarter {

	// 主方法
	public static void main(String[] args) {

		// debug模式可以观察词法，语法，和语义是否分析得正确
		boolean debug = false;

		// 第一个参数是代码地址
		String inputPath = args[0];
		// 第二参数是输出地址
		String outputPath = args[1];

		// 1.获取所有指定路径下的文件
		Map<String, File> files = new LinkedHashMap<>();
		FileUtils.getFiles(inputPath, "", files);

		// 2.如果是debug模式,则进行token的打印
		if (debug) {
			new ShyDebugger().debug(files);// debug
			return;
		}

		// 3.如果不是debug模式,则解析成相应的数据结构
		Map<String, CtClass> mainClasses = new ShyCompiler().compile(files);// compile

		for (CtClass clazz : mainClasses.values()) {
			// 4.转换方法中的内容,并生成java代码
			String code = new JavaBuilder().build(clazz);// build
			// 替换类的别名
			code = AliasReplacer.replace(clazz, code);
			// 打印
			System.out.println(code);
			// 输出到指定文件夹下
			FileUtils.generateFile(outputPath, clazz.packageStr, clazz.typeName, code);
		}

	}

}
