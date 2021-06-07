package com.gitee.spirit.starter.java;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.utils.ConfigUtils;
import com.gitee.spirit.common.utils.URLFileUtils;
import com.gitee.spirit.core.api.CodeBuilder;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.Import;
import com.gitee.spirit.core.compile.AppClassLoader;
import com.gitee.spirit.core.lexer.AliasCharsHandler;

@Component
@Profile("compile")
public class JavaRunner implements ApplicationRunner {

	@Autowired
	public RunningMonitor monitor;
	@Autowired
	public AppClassLoader loader;
	@Autowired
	public CodeBuilder builder;
	@Autowired
	public AliasCharsHandler aliasHandler;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		monitor.printArgs(args);
		long startTime = System.currentTimeMillis();
		compileAndGenerateFiles();
		monitor.printTotalTime(startTime);
	}

	public void compileAndGenerateFiles() {
		String outputPath = ConfigUtils.getOutputPath();
		boolean debug = ConfigUtils.isDebug();
		List<IClass> classes = loader.getAllClasses();
		classes.forEach(clazz -> {
			String targetCode = builder.build(clazz);
			targetCode = replaceAlias(clazz, targetCode);
			if (debug) {
				System.out.println(targetCode);
			}
			if (StringUtils.isNotEmpty(outputPath)) {
				URLFileUtils.generateFile(outputPath, clazz.getClassName().replaceAll("\\.", "/") + ".java", targetCode);
			}
		});
	}

	public String replaceAlias(IClass clazz, String code) {
		for (Import imp : clazz.getAliasImports()) {
			code = aliasHandler.replace(code, imp.getAlias(), imp.getClassName());
		}
		return code;
	}

}
