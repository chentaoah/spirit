package com.gitee.spirit.code.tools.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ElementVisitor;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.CoreCompiler;
import com.gitee.spirit.core.element.entity.Element;

@Component
public class ElementSelector extends CoreCompiler {

	@Autowired
	public ElementVisitor visitor;

	public IType findElementAndGetType(IClass clazz, Integer lineNumber) {
		// TODO
//		Element element = findElement(Arrays.asList(clazz.element), lineNumber);
//		if (element != null) {
//			IVariable variable = visitor.getVariableIfPossible(clazz, element);
//			return variable.getType();
//		}
		return null;
	}

	public Element findElement(List<Element> elements, Integer lineNumber) {
		for (Element element : elements) {
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
