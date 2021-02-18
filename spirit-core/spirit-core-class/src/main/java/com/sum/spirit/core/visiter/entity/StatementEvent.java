package com.sum.spirit.core.visiter.entity;

import com.sum.spirit.common.entity.Statement;
import com.sum.spirit.core.clazz.entity.IClass;

public class StatementEvent {

	public IClass clazz;
	public MethodContext context;
	public Statement statement;

	public StatementEvent(IClass clazz, Statement statement) {
		this.clazz = clazz;
		this.statement = statement;
	}

	public StatementEvent(IClass clazz, Statement statement, MethodContext context) {
		this.clazz = clazz;
		this.statement = statement;
		this.context = context;
	}

	public boolean isMethodScope() {
		return context != null;
	}

}
