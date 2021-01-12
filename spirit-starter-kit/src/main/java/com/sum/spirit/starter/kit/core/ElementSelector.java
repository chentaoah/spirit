package com.sum.spirit.starter.kit.core;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.CoreCompiler;
import com.sum.spirit.core.ElementVisiter;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.clazz.impl.IMethod;
import com.sum.spirit.pojo.clazz.impl.IVariable;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Element;

@Component
public class ElementSelector extends CoreCompiler {

	@Autowired
	public ElementVisiter visiter;

	public IType findElementAndGetType(IClass clazz, Integer lineNumber) {
		for (IMethod method : clazz.methods) {
			Element element = findElement(Arrays.asList(method.element), lineNumber);
			if (element != null) {
				IVariable variable = visiter.getVariableIfPossible(clazz, element);
				return variable.getType();
			}
		}
		return null;
	}

	public Element findElement(List<Element> elements, Integer lineNumber) {
		for (int index = 0; index < elements.size(); index++) {
			Element element = elements.get(index);
			if (element.line.number == lineNumber) {
				return element;
			}
			if (element.hasChild()) {
				Element subElement = findElement(element.children, lineNumber);
				if (subElement != null) {
					return subElement;
				}
			}
		}
		return null;
	}

}
