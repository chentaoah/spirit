package com.sum.spirit.core.clazz.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.element.entity.Element;

public abstract class ImportUnit extends AnnotationUnit {

	public List<Import> imports;

	public ImportUnit(List<Import> imports, List<IAnnotation> annotations, Element element) {
		super(annotations, element);
		this.imports = imports != null ? new ArrayList<>(imports) : new ArrayList<>();
	}

	public List<Import> getImports() {
		return imports.stream().filter(imp -> !imp.hasAlias()).collect(Collectors.toList());
	}

	public List<Import> getAliasImports() {
		return imports.stream().filter(imp -> imp.hasAlias()).collect(Collectors.toList());
	}

	public Import findImport(String className) {
		for (Import import0 : imports) {
			if (import0.matchClassName(className)) {
				return import0;
			}
		}
		return null;
	}

	public Import findImportByLastName(String simpleName) {
		for (Import import0 : imports) {
			if (import0.matchSimpleName(simpleName)) {
				return import0;
			}
		}
		return null;
	}

}
