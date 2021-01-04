package com.sum.spirit.starter.kit.assistant;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.clazz.impl.IMethod;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Line;
import com.sum.spirit.starter.kit.core.CustomCompiler;
import com.sum.spirit.starter.kit.pojo.MethodInfo;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileHelper;
import com.sum.spirit.utils.LineUtils;

import cn.hutool.core.io.IoUtil;

@Service
public class MethodService {

	@Autowired
	public CustomCompiler compiler;
	@Autowired
	public ClassLinker linker;

	public List<MethodInfo> getMethodInfos(String filePath, String content, Integer lineNumber) {
		// 参数
		String inputPath = ConfigUtils.getProperty(Constants.INPUT_ARG_KEY);
		String extension = ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY,
				Constants.DEFAULT_FILENAME_EXTENSION);

		// 根据文件名，获取className
		String className = FileHelper.getPath(inputPath, filePath, extension);

		// 删除后面的行，将该行进行补全，然后截断，剩下待推导部分
		Map<String, String> result = completeCode(content, lineNumber);
		content = result.get("content");
		String incompleteName = result.get("incompleteName");

		// 找到对应class,并找到印记，获取推导出的类型，并返回所有该类型的方法信息
		Map<String, InputStream> inputs = FileHelper.getFiles(inputPath, extension);
		inputs.put(className, IoUtil.toStream(content, Constants.DEFAULT_CHARSET));
		IType type = compiler.compileAndGetType(inputs, className, lineNumber);
		Object clazz = linker.toClass(type);

		List<MethodInfo> methodInfos = new ArrayList<>();
		if (clazz instanceof IClass) {
			for (IMethod method : ((IClass) clazz).methods) {
				if (method.getName().startsWith(incompleteName)) {
					methodInfos.add(createMethodInfo(method, incompleteName));
				}
			}
		} else if (clazz instanceof Class) {
			for (Method method : ((Class<?>) clazz).getMethods()) {
				if (method.getName().startsWith(incompleteName)) {
					methodInfos.add(createMethodInfo(method, incompleteName));
				}
			}
		}
		return methodInfos;
	}

	public Map<String, String> completeCode(String content, Integer lineNumber) {
		Map<String, String> result = new HashMap<>();
		List<String> lines = IoUtil.readLines(new StringReader(content), new ArrayList<String>());
		String line = lines.get(lineNumber - 1);
		if (line.contains("@")) {
			String indent = new Line(line).getIndent();// 缩进
			line = line.trim();
			int index = line.indexOf('@');
			line = line.substring(0, index + 1);
			for (int idx = index - 1; idx >= 0; idx--) {
				char c = line.charAt(idx);
				if (c == '(' || c == '[' || c == '{' || c == '<') {
					boolean isMatch = matches(line, idx, c);
					if (!isMatch) {
						line = line.substring(idx + 1);
					}
				} else if (c == ',') {
					line = line.substring(idx + 1);
				}
			}
			String incompleteName = line.substring(line.lastIndexOf('.') + 1, line.lastIndexOf('@'));
			result.put("incompleteName", incompleteName);
			line = line.substring(0, line.lastIndexOf('.'));
			if (!line.startsWith("return")) {
				line = "return " + line;
			}
			lines.set(lineNumber - 1, indent + line);

		} else {
			throw new RuntimeException("No symbol '@' found!");
		}
		StringBuilder builder = new StringBuilder();
		lines.forEach(line0 -> builder.append(line0 + "\n"));
		result.put("content", builder.toString());
		return result;
	}

	public boolean matches(String line, int index, char leftChar) {
		char rigthChar = LineUtils.flipChar(leftChar);
		int end = LineUtils.findEndFromIndex(line, index, leftChar, rigthChar);
		return end != -1;
	}

	public MethodInfo createMethodInfo(IMethod method, String incompleteName) {
		String tipText = method.toString();
		String actualText = method.toSimpleString();
		if (actualText.startsWith(incompleteName)) {
			actualText = actualText.replace(incompleteName, "");
		}
		return new MethodInfo(tipText, actualText);
	}

	public MethodInfo createMethodInfo(Method method, String incompleteName) {
		String tipText = method.toString();
		String actualText = method.toString();
		return new MethodInfo(tipText, actualText);
	}

}
