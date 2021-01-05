package com.sum.spirit.starter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;
import com.sum.spirit.core.AliasReplacer;
import com.sum.spirit.core.RunningMonitor;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileHelper;

@Component
public class JavaRunner implements ApplicationRunner {

	@Autowired
	public Compiler compiler;
	@Autowired
	public CodeBuilder builder;
	@Autowired
	public AliasReplacer replacer;
	@Autowired
	public RunningMonitor monitor;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 是否自动编译
		if (!ConfigUtils.isAutoRun()) {
			return;
		}
		monitor.printArgs(args);
		// 参数
		long timestamp = System.currentTimeMillis();
		String inputPath = ConfigUtils.getInputPath();
		String outputPath = ConfigUtils.getOutputPath();
		String extension = ConfigUtils.getFileExtension();
		// 编译
		Map<String, InputStream> inputs = FileHelper.getFiles(inputPath, extension);
		List<IClass> classes = compiler.compile(inputs);
		generateFiles(outputPath, classes);
		monitor.printTotalTime(timestamp);
	}

	public void generateFiles(String outputPath, List<IClass> classes) {
		classes.forEach(clazz -> {
			String code = builder.build(clazz);// 输出目标代码
			code = replacer.replace(clazz, code);
			System.out.println(code);
			if (StringUtils.isNotEmpty(outputPath)) {// 生成文件
				FileHelper.generateFile(outputPath, clazz.getClassName(), code);
			}
		});
	}

}
