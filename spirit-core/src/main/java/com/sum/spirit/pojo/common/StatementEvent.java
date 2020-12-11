package com.sum.spirit.pojo.common;

import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.element.impl.Statement;

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
