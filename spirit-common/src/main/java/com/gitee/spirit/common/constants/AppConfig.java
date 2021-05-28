package com.gitee.spirit.common.constants;

public interface AppConfig {

	public static interface Argument {
		String INPUT = "input";
		String OUTPUT = "output";
		String CLASSPATHS = "classpaths";
		String LANG_PACKAGE = "langPackage";
		String UTIL_PACKAGE = "utilPackage";
		String FILE_EXTENSION = "fileExtension";
		String DEBUG = "debug";
	}

	public static interface DefaultValue {
		String CHARSET = "UTF-8";
		String FILE_EXTENSION = "sp";
		boolean DEBUG = true;
	}

}
