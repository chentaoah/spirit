package com.sum.spirit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileHelper {

	public static final String SEPARATOR = File.separator;
	public static final String REGEX = "\\".equals(SEPARATOR) ? SEPARATOR + "\\" : SEPARATOR;

	public static URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static URI toURI(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static FileInputStream getInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream asStream(URL url) {
		return getInputStream(new File(toURI(url)));
	}

	public static Map<String, InputStream> getFiles(String inputPath, String extension) {
		Map<String, InputStream> fileMap = new HashMap<>();
		Collection<File> files = FileUtils.listFiles(new File(inputPath), new String[] { extension }, true);
		files.forEach(file -> {
			String path = getPath(inputPath, file.getAbsolutePath(), extension);
			fileMap.put(path, getInputStream(file));
		});
		return fileMap;
	}

	public static String getPath(String inputPath, String filePath, String extension) {
		return filePath.replace(inputPath + SEPARATOR, "").replaceAll(REGEX, ".").replace("." + extension, "");
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
