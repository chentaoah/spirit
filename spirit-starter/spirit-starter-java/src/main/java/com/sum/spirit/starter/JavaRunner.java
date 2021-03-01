package com.sum.spirit.starter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.ConfigUtils;
import com.sum.spirit.common.utils.FileUrlUtils;
import com.sum.spirit.core.api.CodeBuilder;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.compile.AppClassLoader;
import com.sum.spirit.core.lexer.AliasCharsHandler;

@Component
@Profile("compile")
public class JavaRunner implements ApplicationRunner {

	@Autowired
	public AppClassLoader loader;
	@Autowired
	public CodeBuilder builder;
	@Autowired
	public AliasCharsHandler handler;
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
			code = replaceAlias(clazz, code);
			if (debug) {
				System.out.println(code);
			}
			if (StringUtils.isNotEmpty(outputPath)) {// 生成文件
				FileUrlUtils.generateFile(outputPath, clazz.getClassName().replaceAll("\\.", "/") + ".java", code);
			}
		});
	}

	public String replaceAlias(IClass clazz, String code) {
		for (Import imp : clazz.getAliasImports()) {
			code = handler.replace(code, imp.getAlias(), imp.getClassName());
		}
		return code;
	}

}
