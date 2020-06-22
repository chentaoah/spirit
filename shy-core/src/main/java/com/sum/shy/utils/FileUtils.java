package com.sum.shy.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileUtils {

	public static final String SEPARATOR = File.separator;

	public static Map<String, File> getFiles(String inputPath) {

		Map<String, File> files = new LinkedHashMap<>();

		File directory = new File(inputPath);
		if (!directory.isDirectory())
			return files;

		getFilesRecursively(directory, directory.getName(), files);
		return files;
	}

	public static void getFilesRecursively(File directory, String packageStr, Map<String, File> files) {

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				getFilesRecursively(file, packageStr + "." + file.getName(), files);

			} else if (file.isFile()) {
				if (file.getName().endsWith(".shy"))
					files.put(packageStr + "." + file.getName().replace(".shy", ""), file);
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

			Files.write(code, file, Charsets.UTF_8);

		} catch (IOException e) {
			throw new RuntimeException("Failed to generate file!");
		}
	}

}
