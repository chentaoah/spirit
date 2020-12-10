package com.sum.spirit.java.core.visit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.FastDeducer;
import com.sum.spirit.core.c.visit.AbsElementAction;
import com.sum.spirit.java.utils.TreeUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.StmtVisiter;

public abstract class AbsTreeElementAction extends AbsElementAction {

	@Autowired
	public FastDeducer deducer;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.getStatement();
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			if (isTrigger(currentToken)) {
				visit(clazz, stmt, index, currentToken);
			}
			return null;
		});
	}

	public void visit(IClass clazz, Statement statement, int index, Token currentToken) {
		Map<String, Object> context = new HashMap<>();
		visitPrev(clazz, statement, index, currentToken, context);
		visitNext(clazz, statement, index, currentToken, context);
	}

	public void visitPrev(IClass clazz, Statement statement, int index, Token currentToken, Map<String, Object> context) {
		int start = TreeUtils.findStart(statement, index);
		Statement prevStatement = statement.subStmt(start, index);
		IType prevType = deducer.derive(clazz, prevStatement);
		context.put("start", start);
		context.put("prevStatement", prevStatement);
		context.put("prevType", prevType);
		doVisitPrev(clazz, statement, index, currentToken, context);
	}

	public void visitNext(IClass clazz, Statement statement, int index, Token currentToken, Map<String, Object> context) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType nextType = deducer.derive(clazz, nextStatement);
		context.put("end", end);
		context.put("nextStatement", nextStatement);
		context.put("nextType", nextType);
		doVisitNext(clazz, statement, index, currentToken, context);
	}

	public void doVisitPrev(IClass clazz, Statement statement, int index, Token currentToken, Map<String, Object> context) {
		// ignore
	}

	public void doVisitNext(IClass clazz, Statement statement, int index, Token currentToken, Map<String, Object> context) {
		// ignore
	}

	public abstract boolean isTrigger(Token currentToken);

}
