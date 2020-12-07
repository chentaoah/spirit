package com.sum.spirit.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sum.spirit.pojo.common.Constants;

public class FileUtils {

	public static final String SEPARATOR = File.separator;

	public static String getName(File file) {
		String suffix = "." + ConfigUtils.getProperty(Constants.FILE_SUFFIX_KEY);
		return file.getName().replace(suffix, "");
	}

	public static Map<String, File> getFiles(String inputPath) {

		Map<String, File> files = new LinkedHashMap<>();

		File directory = new File(inputPath);
		if (!directory.isDirectory()) {
			return files;
		}

		// The directory is CLASSPATH
		getFilesRecursively(directory, null, files);
		return files;
	}

	public static void getFilesRecursively(File directory, String packageStr, Map<String, File> files) {

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				getFilesRecursively(file, (packageStr == null ? "" : packageStr + ".") + file.getName(), files);

			} else if (file.isFile()) {
				String suffix = "." + ConfigUtils.getProperty(Constants.FILE_SUFFIX_KEY);
				if (file.getName().endsWith(suffix)) {
					files.put(packageStr + "." + file.getName().replace(suffix, ""), file);
				}
			}
		}
	}

	public static void generateFile(String outputPath, String className, String code) {

		String filePath = outputPath + SEPARATOR + className.replaceAll("\\.", "\\" + SEPARATOR) + ".java";
		File directory = new File(filePath.substring(0, filePath.lastIndexOf(SEPARATOR)));
		File file = new File(filePath);

		try {
			if (!directory.exists()) {
				directory.mkdirs();
			}

			if (!file.exists()) {
				file.createNewFile();
			}

			// Code changed due to guava upgrade
			Files.asCharSink(file, Charsets.UTF_8).write(code);

		} catch (IOException e) {
			throw new RuntimeException("Failed to generate file!");
		}
	}

}
