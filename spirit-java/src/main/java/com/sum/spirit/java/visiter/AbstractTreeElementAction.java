package com.sum.spirit.java.visiter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.visiter.action.AbstractElementAction;
import com.sum.spirit.core.visiter.action.FastDeducer;
import com.sum.spirit.core.visiter.entity.ElementEvent;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.core.visiter.utils.StmtVisiter;
import com.sum.spirit.java.utils.TreeUtils;

public abstract class AbstractTreeElementAction extends AbstractElementAction {

	public static final String START = "START";
	public static final String PREV_STATEMENT = "PREV_STATEMENT";
	public static final String PREV_TYPE = "PREV_TYPE";
	public static final String END = "END";
	public static final String NEXT_STATEMENT = "NEXT_STATEMENT";
	public static final String NEXT_TYPE = "NEXT_TYPE";

	@Autowired
	public FastDeducer deducer;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element.statement;
		new StmtVisiter().visitVoid(statement, visitEvent -> {
			Token token = visitEvent.item;
			if (isTrigger(token)) {
				visit(clazz, (Statement) visitEvent.listable, visitEvent.index, token);
			}
		});
	}

	public void visit(IClass clazz, Statement statement, int index, Token token) {
		Map<String, Object> context = new HashMap<>();
		visitPrev(clazz, statement, index, token, context);
		visitNext(clazz, statement, index, token, context);
	}

	public void visitPrev(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		int start = TreeUtils.findStart(statement, index);
		Statement prevStatement = statement.subStmt(start, index);
		IType prevType = deducer.derive(clazz, prevStatement);
		context.put(START, start);
		context.put(PREV_STATEMENT, prevStatement);
		context.put(PREV_TYPE, prevType);
		doVisitPrev(clazz, statement, index, token, context);
	}

	public void visitNext(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType nextType = deducer.derive(clazz, nextStatement);
		context.put(END, end);
		context.put(NEXT_STATEMENT, nextStatement);
		context.put(NEXT_TYPE, nextType);
		doVisitNext(clazz, statement, index, token, context);
	}

	public void doVisitPrev(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		// ignore
	}

	public void doVisitNext(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		// ignore
	}

	public abstract boolean isTrigger(Token currentToken);

}
