package com.sum.spirit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileUtils {

	public static final String SEPARATOR = File.separator;

	public static Map<String, File> getFiles(String inputPath, String suffix) {
		Map<String, File> files = new LinkedHashMap<>();
		File directory = new File(inputPath);
		if (!directory.isDirectory()) {
			return files;
		}
		getFilesByLoop(directory, null, files, "." + suffix);
		return files;
	}

	public static void getFilesByLoop(File directory, String packageStr, Map<String, File> files, String suffix) {
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				getFilesByLoop(file, (packageStr == null ? "" : packageStr + ".") + file.getName(), files, suffix);

			} else if (file.isFile()) {
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

	public static FileInputStream getFileInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
