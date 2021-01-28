package com.sum.spirit.starter.kit.core;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.CoreCompiler;
import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.clazz.pojo.IVariable;
import com.sum.spirit.core.element.pojo.Element;
import com.sum.spirit.core.visiter.ElementVisiter;
import com.sum.spirit.core.visiter.pojo.IType;

@Component
public class ElementSelector extends CoreCompiler {

	@Autowired
	public ElementVisiter visiter;

	public IType findElementAndGetType(IClass clazz, Integer lineNumber) {
		Element element = findElement(Arrays.asList(clazz.element), lineNumber);
		if (element != null) {
			IVariable variable = visiter.getVariableIfPossible(clazz, element);
			return variable.getType();
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
