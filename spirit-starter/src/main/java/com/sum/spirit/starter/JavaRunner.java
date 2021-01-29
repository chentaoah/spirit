package com.sum.spirit.starter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.core.AliasReplacer;
import com.sum.spirit.core.AppClassLoader;
import com.sum.spirit.core.RunningMonitor;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileHelper;

@Component
@Profile("compile")
public class JavaRunner implements ApplicationRunner {

	@Autowired
	public AppClassLoader loader;
	@Autowired
	public CodeBuilder builder;
	@Autowired
	public AliasReplacer replacer;
	@Autowired
	public RunningMonitor monitor;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		monitor.printArgs(args);
		long timestamp = System.currentTimeMillis();
		compileAndGenerateFiles();
		monitor.printTotalTime(timestamp);
	}

	public void compileAndGenerateFiles() {
		String outputPath = ConfigUtils.getOutputPath();
		boolean debug = ConfigUtils.isDebug();
		List<IClass> classes = loader.getAllClasses();
		classes.forEach(clazz -> {
			String code = builder.build(clazz);// 输出目标代码
			code = replacer.replace(clazz, code);
			if (debug) {
				System.out.println(code);
			}
			if (StringUtils.isNotEmpty(outputPath)) {// 生成文件
				FileHelper.generateFile(outputPath, clazz.getClassName(), code);
			}
		});
	}

}
