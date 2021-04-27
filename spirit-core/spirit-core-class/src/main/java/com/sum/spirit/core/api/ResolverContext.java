package com.sum.spirit.core.api;

import java.util.List;

public interface ResolverContext {

	TypeFactory getTypeFactory();

	List<ImportSelector> getImportSelectors();

}
