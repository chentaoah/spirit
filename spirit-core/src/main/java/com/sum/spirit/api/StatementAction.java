package com.sum.spirit.api;

import com.sum.spirit.core.visiter.entity.StatementEvent;

public interface StatementAction {

	void visit(StatementEvent event);

}
