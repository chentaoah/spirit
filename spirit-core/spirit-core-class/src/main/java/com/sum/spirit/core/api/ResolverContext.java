package com.sum.spirit.core.api;

import java.util.List;

public interface ResolverContext {

	ElementBuilder getElementBuilder();

	TypeFactory getTypeFactory();

	List<ImportSelector> getImportSelectors();

}
