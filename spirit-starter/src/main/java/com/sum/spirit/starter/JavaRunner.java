package com.sum.spirit.starter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.AliasReplacer;
import com.sum.spirit.core.RunningMonitor;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileHelper;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;

@Component
@DependsOn("configUtils")
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
		boolean autoRun = ConfigUtils.getProperty(Constants.AUTO_RUN_KEY, false);
		if (!autoRun) {
			return;
		}

		monitor.printArgs(args.getSourceArgs());

		long timestamp = System.currentTimeMillis();
		String inputPath = args.getOptionValues(Constants.INPUT_ARG).get(0);
		String outputPath = args.containsOption(Constants.OUTPUT_ARG) ? args.getOptionValues(Constants.OUTPUT_ARG).get(0) : null;
		String extension = ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY, Constants.DEFAULT_FILENAME_EXTENSION);

		Map<String, ? extends InputStream> inputs = getInputStreams(inputPath, extension);
		List<IClass> classes = compile(inputs);
		generateFiles(outputPath, classes);

		monitor.printTotalTime(timestamp);
	}

	public Map<String, ? extends InputStream> getInputStreams(String inputPath, String extension) {
		return FileHelper.getFiles(inputPath, extension);
	}

	public List<IClass> compile(Map<String, ? extends InputStream> inputs) {
		return compiler.compile(inputs);
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
