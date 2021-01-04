package com.sum.spirit.starter.kit.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.CoreCompiler;
import com.sum.spirit.core.FastDeducer;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.clazz.impl.IMethod;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Element;

import cn.hutool.core.collection.CollUtil;

@Component
public class CustomCompiler extends CoreCompiler {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public FastDeducer deducer;

	public IType compileAndGetType(Map<String, InputStream> inputs, String className, Integer lineNumber) {
		// 加载类型
		List<IClass> classes = loadClasses(inputs, className);
		// 访问准备
		classes.forEach(clazz -> visiter.prepareForVisit(clazz));
		// 截断后续的行，并返回方法名称
		IClass clazz = CollUtil.findOne(classes, clazz0 -> className.equals(clazz0.getClassName()));

		return resolveLines(clazz, lineNumber);
	}

	public IType resolveLines(IClass clazz, Integer lineNumber) {
		for (IMethod method : clazz.methods) {
			Element element = tryCutOffLines(method.element.children, lineNumber);
			if (element != null) {
				visiter.visitMember(clazz, method);
				return deducer.derive(clazz, element.statement);
			}
		}
		return null;
	}

	public Element tryCutOffLines(List<Element> elements, Integer lineNumber) {
		for (int index = 0; index < elements.size(); index++) {
			Element element = elements.get(index);
			if (element.line.number == lineNumber) {
//				for (int idx = index + 1; idx < elements.size(); idx++) {
//					elements.remove(idx);
//				}
				return element;
			}
			if (element.hasChild()) {
				Element subElement = tryCutOffLines(element.children, lineNumber);
				if (subElement != null) {
					return subElement;
				}
			}
		}
		return null;
	}

}
