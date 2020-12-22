package com.sum.spirit.starter.kit.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.CompilerImpl;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.clazz.impl.IMethod;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Element;
import com.sum.spirit.pojo.element.impl.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.starter.kit.pojo.MethodInfo;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileHelper;

import cn.hutool.core.io.IoUtil;

@Service
public class KitService {

	@Autowired
	public CompilerImpl compiler;
	@Autowired
	public ClassVisiter visiter;

	public List<MethodInfo> getMethodInfos(String fileName, String content, Integer lineNumber) {
		String inputPath = ConfigUtils.getProperty(Constants.INPUT_ARG_KEY);
		String extension = ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY,
				Constants.DEFAULT_FILENAME_EXTENSION);
		// 虚拟一个类名
		String className = "com.sum.spirit.virtual." + fileName;
		// 获取到方法名称
		String methodName = getMethodName(content, lineNumber);
		// 删除后面的行，将该行进行补全，然后截断，剩下待推导部分
		content = completeCode(content, lineNumber);
		// 进行部分编译
		Map<String, InputStream> inputs = FileHelper.getFiles(inputPath, extension);
		inputs.put(className, IoUtil.toStream(content, Constants.DEFAULT_CHARSET));
		List<IClass> classes = compiler.loadClasses(inputs, className);
		// 找到对应class,并找到印记，获取推导出的类型，并返回所有该类型的方法信息
		for (IClass clazz : classes) {
			if (className.equals(clazz.getClassName())) {
				for (IMethod method : clazz.methods) {
					if (methodName.equals(method.getName())) {
						Element element = tryGetElement(method, lineNumber);
						if (element != null) {
							visiter.visitMember(clazz, method);
							Token token = element.getToken(element.size() - 1);
							IType type = token.attr(AttributeEnum.TYPE);
							System.out.println(type.getClassName());
						}
					}
				}
			}
		}
		return null;
	}

	public String getMethodName(String content, Integer lineNumber) {
		return null;
	}

	public String completeCode(String content, Integer lineNumber) {
		return null;
	}

	public Element tryGetElement(IMethod method, Integer lineNumber) {
		return null;
	}

}
