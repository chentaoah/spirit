package com.sum.spirit.api;

import com.sum.spirit.pojo.common.StatementEvent;

public interface StatementAction {

	void visit(StatementEvent event);

}
