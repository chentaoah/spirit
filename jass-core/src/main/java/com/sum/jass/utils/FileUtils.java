package com.sum.jass.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileUtils {

	public static final String SEPARATOR = File.separator;

	public static final String SUFFIX = ".ss";

	public static String getName(File file) {
		return file.getName().replace(SUFFIX, "");
	}

	public static Map<String, File> getFiles(String inputPath) {

		Map<String, File> files = new LinkedHashMap<>();

		File directory = new File(inputPath);
		if (!directory.isDirectory())
			return files;

		// The directory is CLASSPATH
		getFilesRecursively(directory, null, files);
		return files;
	}

	public static void getFilesRecursively(File directory, String packageStr, Map<String, File> files) {

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				getFilesRecursively(file, (packageStr == null ? "" : packageStr + ".") + file.getName(), files);

			} else if (file.isFile()) {
				if (file.getName().endsWith(SUFFIX))
					files.put(packageStr + "." + file.getName().replace(SUFFIX, ""), file);
			}
		}
	}

	public static void generateFile(String outputPath, String className, String code) {

		String filePath = outputPath + SEPARATOR + className.replaceAll("\\.", "\\" + SEPARATOR) + ".java";
		File directory = new File(filePath.substring(0, filePath.lastIndexOf(SEPARATOR)));
		File file = new File(filePath);

		try {
			if (!directory.exists())
				directory.mkdirs();

			if (!file.exists())
				file.createNewFile();

			// Code changed due to guava upgrade
			Files.asCharSink(file, Charsets.UTF_8).write(code);

		} catch (IOException e) {
			throw new RuntimeException("Failed to generate file!");
		}
	}

}
