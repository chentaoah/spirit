package com.sum.spirit.starter.kit;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.CoreCompiler;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Element;

import cn.hutool.core.collection.CollUtil;

@Component
public class CustomCompiler extends CoreCompiler {

	public IType compileAndGetType(Map<String, InputStream> inputs, String className, Integer lineNumber) {
		// 加载类型
		List<IClass> classes = loadClasses(inputs, className);
		// 截断后续的行，并返回方法名称
		IClass clazz = CollUtil.findOne(classes, clazz0 -> className.equals(clazz0.getClassName()));
		@SuppressWarnings("unused")
		Element element = resolveLines(clazz.element, lineNumber);
		return null;
	}

	public Element resolveLines(Element element, Integer lineNumber) {
		return null;
	}

}
